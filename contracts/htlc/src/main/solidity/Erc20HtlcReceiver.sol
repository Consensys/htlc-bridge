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


contract Erc20HtlcReceiver {
    uint256 constant OPEN = 0;
    uint256 constant FINALILISED = 2;
    uint256 constant TIMEDOUT = 3;


    uint256 public timeLockPeriod;
    mapping (address => address) public allowedTokens;

    mapping (address => bool) public authorisedRelayer;


    struct Transfer {
        address relayer;
        address recipient;
        address otherBlockchainTokenContract;
        uint256 amount;
        bytes32 commitment;
        bytes32 preimage;
        uint256 timeLock;
        uint256 state;
    }


    mapping (bytes32 => Transfer) transfers;


    modifier onlyAuthorisedRelayer() {
        require(authorisedRelayer[msg.sender], "Not an authorised relayer");
        _;
    }


    constructor(uint256 _timeLock) {
        timeLockPeriod = _timeLock;
        authorisedRelayer[msg.sender] = true;
    }

    function addAuthorisedRelayer(address _newRelayer) onlyAuthorisedRelayer() external {
        authorisedRelayer[_newRelayer] = true;
    }

    function removeAuthorisedRelayer(address _relayerToBeRemoved) onlyAuthorisedRelayer() external {
        authorisedRelayer[_relayerToBeRemoved] = false;
    }

    function addAllowedToken(address _otherBlockchainTokenContract, address _thisBlockchainTokenContract) onlyAuthorisedRelayer() external {
        allowedTokens[_otherBlockchainTokenContract] = _thisBlockchainTokenContract;
        AllowedTokenAdded(_otherBlockchainTokenContract, _thisBlockchainTokenContract);
    }

    function removeAllowedToken(address _otherBlockchainTokenContract) onlyAuthorisedRelayer() external {
        allowedTokens[_otherBlockchainTokenContract] = address(0);
        AllowedTokenRemoved(_otherBlockchainTokenContract);
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
        require(!transferExists(_commitment), "Transfer already exists");
        // The token must be in the list of known tokens.
        require(isAllowedToken(_otherBlockchainTokenContract), "Token not transferable");

        uint256 timeLock = block.timestamp + timeLockPeriod;

        transfers[_commitment] = Transfer(msg.sender, _recipient, _otherBlockchainTokenContract, _amount, _commitment, 0x0, timeLock, 0);

        emit TransferInit(_commitment, msg.sender, _recipient, _otherBlockchainTokenContract, _amount, timeLock);
    }

    function finaliseTransferFromOtherBlockchain(bytes32 _commitment, bytes32 _preimage) external {
        require(transferExists(_commitment), "Transfer does not exist");
        require(preimageMatchesCommitment(_commitment, _preimage), "Preimage does not match commitment");
        require(transfers[_commitment].state == OPEN, "Transfer not in open state");
        require(!transferExpired(_commitment), "Transfer timed-out");

        address otherBlockchainTokenContract = transfers[_commitment].otherBlockchainTokenContract;
        address thisBlockchainTokenContract = allowedTokens[otherBlockchainTokenContract];
        if (!ERC20(thisBlockchainTokenContract).transfer(transfers[_commitment].recipient, transfers[_commitment].amount)) {
            revert("tranfer failed");
        }

        transfers[_commitment].preimage = _preimage;
        transfers[_commitment].state = FINALILISED;

        emit TransferCompleted(_commitment);
    }

    function getInfo(bytes32 _commitment) public view returns
        (address relayer, address receiver, address otherBlockchainTokenContract, uint256 amount, bytes32 preimage, uint256 timeLock, uint256 state) {
        require(transferExists(_commitment), "Transfer does not exist");

        Transfer storage t = transfers[_commitment];
        return (t.relayer, t.recipient, t.otherBlockchainTokenContract, t.amount,  t.preimage, t.timeLock, t.state);
    }



    function isAllowedToken(address _tokenContract) public view returns(bool) {
        return allowedTokens[_tokenContract] != address(0);
    }

    function transferExists(bytes32 _commitment) public view returns(bool){
        return transfers[_commitment].relayer != address(0);
    }

    function transferExpired(bytes32 _commitment) public view returns(bool){
        return transfers[_commitment].timeLock < block.timestamp;
    }

    function transferState(bytes32 _commitment) public view returns(uint256){
        if (transferExpired(_commitment)) {
            return TIMEDOUT;
        }
        return transfers[_commitment].state;
    }

    function preimageMatchesCommitment(bytes32 _commitment, bytes32 _preimage) public pure returns(bool){
        return _commitment == keccak256(abi.encodePacked(_preimage));
    }


    event TransferInit(
        bytes32 indexed commitment,
        address indexed relayer,
        address indexed recipient,
        address tokenContract,
        uint256 amount,
        uint256 timeLock
    );
    event TransferCompleted(bytes32 indexed commitment);
    event TransferRefunded(bytes32 indexed commitment);

    event AllowedTokenAdded(address otherBlockchainTokenContract, address thisBlockchainTokenContract);
    event AllowedTokenRemoved(address tokenContract);

}