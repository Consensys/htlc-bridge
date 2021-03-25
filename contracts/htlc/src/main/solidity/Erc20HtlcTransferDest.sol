/*
 * Copyright 2020 ConsenSys Software Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
pragma solidity >=0.8.0;

import "contracts/openzeppelin/src/main/solidity/token/ERC20/ERC20.sol";
import "./Erc20HtlcTransferState.sol";


abstract contract Erc20HtlcTransferDest is Erc20HtlcTransferState {
    // Time lock for transfers to this blockchain.
    uint256 public destTimeLockPeriod;

    // Tokens that can be received on this blockchain.
    // Map (token contract address on source blockchain => token contract address on this (the destination) blockchain)
    mapping (address => address) public destAllowedTokens;

    // Relayers that are allowed to complete transfers on the destination side.
    // Care should be taken as these relayers transfer tokens to users. They must be trusted.
    // Map (relayer account address => true if the relayer is authorised).
    mapping (address => bool) public authorisedRelayer;

    struct DestTransfer {
        address relayer;
        address recipient;
        address otherBlockchainTokenContract;
        uint256 amount;
        bytes32 commitment;
        bytes32 preimage;
        uint256 timeLock;
        uint256 state;
    }
    mapping (bytes32 => DestTransfer) destTransfers;


    // The following variables are never used. The idea is that if / when this
    // contract is upgraded, these locations will be used. Reserving them
    // now in this contract should guard against clashes between this
    // abstract contract and other abstract contracts that are combined
    // into a single implementation.
    uint256 dummy101;
    uint256 dummy102;
    uint256 dummy103;
    uint256 dummy104;
    uint256 dummy105;
    uint256 dummy106;
    uint256 dummy107;
    uint256 dummy108;
    uint256 dummy109;
    uint256 dummy110;



    modifier onlyAuthorisedRelayer() {
        require(authorisedRelayer[msg.sender], "Not an authorised relayer");
        _;
    }


    function addAuthorisedRelayer(address _newRelayer) onlyAuthorisedRelayer() external {
        authorisedRelayer[_newRelayer] = true;
    }

    function removeAuthorisedRelayer(address _relayerToBeRemoved) onlyAuthorisedRelayer() external {
        authorisedRelayer[_relayerToBeRemoved] = false;
    }


    function addDestAllowedToken(address _otherBlockchainTokenContract, address _thisBlockchainTokenContract) onlyAuthorisedRelayer() external {
        destAllowedTokens[_otherBlockchainTokenContract] = _thisBlockchainTokenContract;
        DestAllowedTokenAdded(_otherBlockchainTokenContract, _thisBlockchainTokenContract);
    }

    function removeDestAllowedToken(address _otherBlockchainTokenContract) onlyAuthorisedRelayer() external {
        destAllowedTokens[_otherBlockchainTokenContract] = address(0);
        DestAllowedTokenRemoved(_otherBlockchainTokenContract);
    }


    /**
     * Transfer tokens from msg.sender to this contract.
     *
     * msg.sender must have called approve() on the token contract.
     * _commitment ****MUST**** be based on a unique random preimage.
     *
     */
    function newTransferFromOtherBlockchain(address _otherBlockchainTokenContract, address _recipient, uint256 _amount, bytes32 _commitment) onlyAuthorisedRelayer() external {
        // A transfer with the commitment can not already exist.
        require(!destTransferExists(_commitment), "Transfer already exists");
        // The token must be in the list of known tokens.
        require(isDestAllowedToken(_otherBlockchainTokenContract), "Token not transferable");

        uint256 timeLock = block.timestamp + destTimeLockPeriod;

        destTransfers[_commitment] = DestTransfer(msg.sender, _recipient, _otherBlockchainTokenContract, _amount, _commitment, 0x0, timeLock, 0);

        emit DestTransferInit(_commitment, msg.sender, _recipient, _otherBlockchainTokenContract, _amount, timeLock);
    }

    function finaliseTransferFromOtherBlockchain(bytes32 _commitment, bytes32 _preimage) external {
        require(destTransferExists(_commitment), "Transfer does not exist");
        require(preimageMatchesCommitment(_commitment, _preimage), "Preimage does not match commitment");
        require(destTransfers[_commitment].state == OPEN, "Transfer not in open state");
        require(!destTransferExpired(_commitment), "Transfer timed-out");

        address otherBlockchainTokenContract = destTransfers[_commitment].otherBlockchainTokenContract;
        address thisBlockchainTokenContract = destAllowedTokens[otherBlockchainTokenContract];
        if (!ERC20(thisBlockchainTokenContract).transfer(destTransfers[_commitment].recipient, destTransfers[_commitment].amount)) {
            revert("tranfer failed");
        }

        destTransfers[_commitment].preimage = _preimage;
        destTransfers[_commitment].state = FINALILISED;

        emit DestTransferCompleted(_commitment, _preimage);
    }

    function getDestInfo(bytes32 _commitment) public view returns
        (address relayer, address receiver, address otherBlockchainTokenContract, uint256 amount, bytes32 preimage, uint256 timeLock, uint256 state) {
        require(destTransferExists(_commitment), "Transfer does not exist");

        DestTransfer storage t = destTransfers[_commitment];
        return (t.relayer, t.recipient, t.otherBlockchainTokenContract, t.amount,  t.preimage, t.timeLock, t.state);
    }

    function isAuthorisedRelayer(address _relayer) external view returns(bool) {
        return authorisedRelayer[_relayer];
    }

    function isDestAllowedToken(address _tokenContract) public view returns(bool) {
        return destAllowedTokens[_tokenContract] != address(0);
    }

    function destTransferExists(bytes32 _commitment) public view returns(bool){
        return destTransfers[_commitment].relayer != address(0);
    }

    function destTransferExpired(bytes32 _commitment) public view returns(bool){
        return destTransfers[_commitment].timeLock < block.timestamp;
    }

    function destTransferState(bytes32 _commitment) public view returns(uint256){
        if (destTransferExpired(_commitment)) {
            return TIMEDOUT;
        }
        return destTransfers[_commitment].state;
    }


    event DestTransferInit(
        bytes32 indexed commitment,
        address indexed relayer,
        address indexed recipient,
        address tokenContract,
        uint256 amount,
        uint256 timeLock
    );
    event DestTransferCompleted(bytes32 indexed commitment, bytes32 preimage);
    event DestTransferRefunded(bytes32 indexed commitment);

    event DestAllowedTokenAdded(address otherBlockchainTokenContract, address thisBlockchainTokenContract);
    event DestAllowedTokenRemoved(address tokenContract);

}