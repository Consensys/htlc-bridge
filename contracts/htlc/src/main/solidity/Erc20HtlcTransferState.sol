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


abstract contract Erc20HtlcTransferState {
    uint256 constant OPEN = 0;
    uint256 constant REFUNDED = 1;
    uint256 constant FINALILISED = 2;
    uint256 constant TIMEDOUT = 3;

    function preimageMatchesCommitment(bytes32 _commitment, bytes32 _preimage) public pure returns(bool){
        return _commitment == keccak256(abi.encodePacked(_preimage));
    }

    function preimageMatchesCommitment1(bytes32 _commitment, bytes32 _preimage, address _user, address _token, uint256 _amount) public pure returns(bool){
        return _commitment == keccak256(abi.encodePacked(_preimage, _user, _token, _amount));
    }

}