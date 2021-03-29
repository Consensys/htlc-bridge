/*
 * Copyright 2021 ConsenSys Software Inc
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

import "./Erc20HtlcTransferDest.sol";
import "./Erc20HtlcTransferSource.sol";
import "../../../../openzeppelin/src/main/solidity/proxy/utils/Initializable.sol";
import "../../../../voting/src/main/solidity/AdminVoting.sol";


contract Erc20HtlcTransfer is Erc20HtlcTransferDest, Erc20HtlcTransferSource, Initializable {
    uint256 constant VERSION = 20210325;
    uint16 constant VOTE_SOURCE_TIMELOCK = 100;
    uint16 constant VOTE_DEST_TIMELOCK = 101;



    function initialise(uint256 _sourceTimeLock, uint256 _destTimeLock) initializer()  external {
        AdminVoting.initialise();
        sourceTimeLockPeriod = _sourceTimeLock;
        destTimeLockPeriod = _destTimeLock;
    }

    function version() external pure returns (uint256) {
        return VERSION;
    }


    /************************************* INTERNAL FUNCTIONS BELOW HERE *************************************/
    /************************************* INTERNAL FUNCTIONS BELOW HERE *************************************/
    /************************************* INTERNAL FUNCTIONS BELOW HERE *************************************/

    function proposeAppVote(uint16 _action, address _voteTarget, uint256 /* _additionalInfo1 */) internal pure override {
        if (_action == VOTE_SOURCE_TIMELOCK) {
            require(_voteTarget == address(0), "AppVote: vote target must be zero when changing source timelock");
        }
        else if (_action == VOTE_DEST_TIMELOCK) {
            require(_voteTarget == address(0), "AppVote: vote target must be zero when changing destination timelock");
        }
        else {
            revert("AppVote: Unsupported vote action");
        }
    }
    function actionAppVote(uint16 _action, address _voteTarget, uint256 /* _additionalInfo1 */) internal override {
        if (_action == VOTE_SOURCE_TIMELOCK) {
            sourceTimeLockPeriod = votes[_voteTarget].additionalInfo1;
        }
        else if (_action == VOTE_DEST_TIMELOCK) {
            destTimeLockPeriod = votes[_voteTarget].additionalInfo1;
        }
    }
}