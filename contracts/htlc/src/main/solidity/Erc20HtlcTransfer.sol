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
    uint16 constant VOTE_ADD_SOURCE_ALLOWED_TOKEN = 102;
    uint16 constant VOTE_REMOVE_SOURCE_ALLOWED_TOKEN = 103;
    uint16 constant VOTE_ADD_DEST_ALLOWED_TOKEN = 104;
    uint16 constant VOTE_REMOVE_DEST_ALLOWED_TOKEN = 105;


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

    function proposeAppVote(uint16 _action, address _voteTarget, uint256 /* _additionalInfo1 */) internal view override {
        if (_action == VOTE_SOURCE_TIMELOCK) {
            require(_voteTarget == address(0), "AppVote: vote target must be zero when changing source timelock");
        }
        else if (_action == VOTE_DEST_TIMELOCK) {
            require(_voteTarget == address(0), "AppVote: vote target must be zero when changing destination timelock");
        }
        else if (_action == VOTE_ADD_SOURCE_ALLOWED_TOKEN) {
            require(!isSourceAllowedToken(_voteTarget), "AppVote: Can not add existing source token.");
        }
        else if (_action == VOTE_REMOVE_SOURCE_ALLOWED_TOKEN) {
            require(isSourceAllowedToken(_voteTarget), "AppVote: Can not remove unknown source token.");
        }
        else if (_action == VOTE_ADD_DEST_ALLOWED_TOKEN) {
            require(!isDestAllowedToken(_voteTarget), "AppVote: Can not add existing dest token.");
        }
        else if (_action == VOTE_REMOVE_DEST_ALLOWED_TOKEN) {
            require(isDestAllowedToken(_voteTarget), "AppVote: Can not remove unknown dest token.");
        }
        else {
            revert("AppVote: Unsupported vote action");
        }
    }


    function actionAppVote(uint16 _action, address _voteTarget, uint256 _additionalInfo1) internal override {
        if (_action == VOTE_SOURCE_TIMELOCK) {
            sourceTimeLockPeriod = _additionalInfo1;
        }
        else if (_action == VOTE_DEST_TIMELOCK) {
            destTimeLockPeriod = _additionalInfo1;
        }
        else if (_action == VOTE_ADD_SOURCE_ALLOWED_TOKEN) {
            sourceAllowedTokens[_voteTarget] = true;
        }
        else if (_action == VOTE_REMOVE_SOURCE_ALLOWED_TOKEN) {
            sourceAllowedTokens[_voteTarget] = false;
        }
        else if (_action == VOTE_ADD_DEST_ALLOWED_TOKEN) {
            destAllowedTokens[_voteTarget] = address(uint160(_additionalInfo1));
        }
        else if (_action == VOTE_REMOVE_DEST_ALLOWED_TOKEN) {
            delete destAllowedTokens[_voteTarget];
        }
    }
}