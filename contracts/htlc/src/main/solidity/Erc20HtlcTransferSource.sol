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


abstract contract Erc20HtlcTransferSource is Erc20HtlcTransferState {
    // Time lock for transfers from this blockchain.
    uint256 public sourceTimeLockPeriod;

    // Tokens that can be sent from this blockchain.
    // Map (token contract address => true if token can be sent)
    mapping (address => bool) public sourceAllowedTokens;

    struct SourceTransfer {
        address sender;
        address tokenContract;
        uint256 amount;
        bytes32 commitment;
        bytes32 preimageSalt;
        uint256 timeLock;
        uint256 state;
    }
    mapping (bytes32 => SourceTransfer) sourceTransfers;

    // The following variables are never used. The idea is that if / when this
    // contract is upgraded, these locations will be used. Reserving them
    // now in this contract should guard against clashes between this
    // abstract contract and other abstract contracts that are combined
    // into a single implementation.
    uint256 dummy1;
    uint256 dummy2;
    uint256 dummy3;
    uint256 dummy4;
    uint256 dummy5;
    uint256 dummy6;
    uint256 dummy7;
    uint256 dummy8;
    uint256 dummy9;
    uint256 dummy10;


    /**
     * Transfer tokens from msg.sender to this contract.
     *
     * msg.sender must have called approve() on the token contract.
     * _commitment ****MUST**** be based on a unique random preimage.
     *
     */
    function newTransferToOtherBlockchain(address _tokenContract, uint256 _amount, bytes32 _commitment) external {
        // A transfer with the commitment can not already exist.
        require(!sourceTransferExists(_commitment), "Transfer already exists");
        // The token must be in the list of known tokens.
        require(isSourceAllowedToken(_tokenContract), "Token not transferable");
        // The transfer will fail is _amount is negative, or if adequate allowance hasn't been set-up. 
        if (!ERC20(_tokenContract).transferFrom(msg.sender, address(this), _amount)) {
            revert("transfer failed");
        }

        uint256 timeLock = block.timestamp + sourceTimeLockPeriod;

        sourceTransfers[_commitment] = SourceTransfer(msg.sender, _tokenContract, _amount, _commitment, 0x0, timeLock, 0);

        emit SourceTransferInit(_commitment, msg.sender, _tokenContract, _amount, timeLock);
    }

    function finaliseTransferToOtherBlockchain(bytes32 _commitment, bytes32 _preimageSalt) external {
        require(sourceTransferExists(_commitment), "Transfer does not exist");
        require(preimageMatchesCommitment(_commitment, _preimageSalt,
            sourceTransfers[_commitment].sender, sourceTransfers[_commitment].tokenContract,
            sourceTransfers[_commitment].amount), "Preimage does not match commitment");
        require(sourceTransfers[_commitment].state == OPEN, "Transfer not in open state");

        sourceTransfers[_commitment].preimageSalt = _preimageSalt;
        sourceTransfers[_commitment].state = FINALILISED;

        emit SourceTransferCompleted(_commitment);
    }

    function refundTransferToOtherBlockchain(bytes32 _commitment) external {
        require(sourceTransferExists(_commitment), "Transfer does not exist");
        require(sourceTransfers[_commitment].state == OPEN, "Transfer not in open state");
        require(sourceTransferExpired(_commitment), "Transaction has not yet timed-out");

        if (!ERC20(sourceTransfers[_commitment].tokenContract).transfer(
                sourceTransfers[_commitment].sender, sourceTransfers[_commitment].amount)) {
            revert("refund failed");
        }

        sourceTransfers[_commitment].state = REFUNDED;

        emit SourceTransferRefunded(_commitment);
    }



    function getSourceInfo(bytes32 _commitment) public view returns
        (address sender, address tokenContract, uint256 amount, bytes32 preimageSalt, uint256 timeLock, uint256 state) {
        require(sourceTransferExists(_commitment), "Transfer does not exist");

        SourceTransfer storage t = sourceTransfers[_commitment];
        return (t.sender, t.tokenContract, t.amount,  t.preimageSalt, t.timeLock, t.state);
    }



    function isSourceAllowedToken(address _tokenContract) public view returns(bool) {
        return sourceAllowedTokens[_tokenContract];
    }

    function sourceTransferExists(bytes32 _commitment) public view returns(bool){
        return sourceTransfers[_commitment].sender != address(0);
    }

    function sourceTransferExpired(bytes32 _commitment) public view returns(bool){
        return sourceTransfers[_commitment].timeLock < block.timestamp;
    }

    function sourceTransferState(bytes32 _commitment) public view returns(uint256){
        if (sourceTransfers[_commitment].state == OPEN && sourceTransferExpired(_commitment)) {
            return TIMEDOUT;
        }
        return sourceTransfers[_commitment].state;
    }



    event SourceTransferInit(
        bytes32 indexed commitment,
        address indexed sender,
        address tokenContract,
        uint256 amount,
        uint256 timeLock
    );
    event SourceTransferCompleted(bytes32 indexed commitment);
    event SourceTransferRefunded(bytes32 indexed commitment);
}