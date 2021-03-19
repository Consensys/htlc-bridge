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

import "contracts/openzeppelin-tokens/src/main/solidity/token/ERC20/ERC20.sol";


contract Erc20HtlcTransfer {
    uint256 constant OPEN = 0;
    uint256 constant REFUNDED = 1;
    uint256 constant FINALILISED = 2;


    uint256 public timeLockPeriod;
    mapping (address => bool) public allowedTokens;



    struct Transfer {
        address sender;
        address tokenContract;
        uint256 amount;
        bytes32 commitment;
        bytes32 preimage;
        uint256 timeLock;
        uint256 state;
    }


    mapping (bytes32 => Transfer) transfers;



    constructor(uint256 _timeLock) {
        timeLockPeriod = _timeLock;
    }

    function addAllowedToken(address _tokenContract) external {
        allowedTokens[_tokenContract] = true;
        AllowedTokenAdded(_tokenContract);
    }

    function removeAllowedToken(address _tokenContract) external {
        allowedTokens[_tokenContract] = false;
        AllowedTokenRemoved(_tokenContract);
    }


    /**
     * Transfer tokens from msg.sender to this contract.
     *
     * msg.sender must have called approve() on the token contract.
     * _commitment ****MUST**** be based on a unique random preimage.
     *
     */
    function newTransferToOtherBlockchain(address _tokenContract, uint256 _amount, bytes32 _commitment) external {
        // A transfer with the commitment can not already exist.
        require(!transferExists(_commitment), "Transfer already exists");
        // The token must be in the list of known tokens.
        require(isAllowedToken(_tokenContract), "Token not transferrable");
        // The transfer will fail is _amount is negative, or if adequate allowance hasn't been set-up. 
        if (!ERC20(_tokenContract).transferFrom(msg.sender, address(this), _amount)) {
            revert("transfer failed");
        }

        uint256 timeLock = block.timestamp + timeLockPeriod;

        transfers[_commitment] = Transfer(msg.sender, _tokenContract, _amount, _commitment, 0x0, timeLock, 0);

        emit TransferInit(_commitment, msg.sender, _tokenContract, _amount, timeLock
        );
    }

    function finaliseTransferToOtherBlockchain(bytes32 _commitment, bytes32 _preimage) external {
        require(transferExists(_commitment), "Transfer does not exist");
        require(preimageMatchesCommitment(_commitment, _preimage), "Preimage does not match commitment");
        require(transfers[_commitment].state == OPEN);

        transfers[_commitment].preimage = _preimage;
        transfers[_commitment].state = FINALILISED;

        emit TransferCompleted(_commitment);
    }

    function refundTransferToOtherBlockchain(bytes32 _commitment) external {
        require(transferExists(_commitment), "Transfer does not exist");
        require(transfers[_commitment].state == OPEN);

        transfers[_commitment].state = REFUNDED;

        emit TransferRefunded(_commitment);
    }



    function getInfo(bytes32 _commitment) public view returns
        (address sender, address tokenContract, uint256 amount, bytes32 preimage, uint256 timeLock, uint256 state) {
        require(transferExists(_commitment), "Transfer does not exist");

        Transfer storage t = transfers[_commitment];
        return (t.sender, t.tokenContract, t.amount,  t.preimage, t.timeLock, t.state);
    }



    function isAllowedToken(address _tokenContract) public view returns(bool) {
        return allowedTokens[_tokenContract];
    }

    function transferExists(bytes32 _commitment) public view returns(bool){
        return transfers[_commitment].sender != address(0);
    }

    function transferState(bytes32 _commitment) public view returns(uint256){
        return transfers[_commitment].state;
    }

    function preimageMatchesCommitment(bytes32 _commitment, bytes32 _preimage) public pure returns(bool){
        return _commitment == sha256(abi.encodePacked(_preimage));
    }


    event TransferInit(
        bytes32 indexed commitment,
        address indexed sender,
        address tokenContract,
        uint256 amount,
        uint256 timeLock
    );
    event TransferCompleted(bytes32 indexed commitment);
    event TransferRefunded(bytes32 indexed commitment);

    event AllowedTokenAdded(address tokenContract);
    event AllowedTokenRemoved(address tokenContract);

}