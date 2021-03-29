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

import "../../main/solidity/AdminVoting.sol";

contract VotingTest is AdminVoting {
    uint16 constant VOTE_VAL = 100;
    uint256 public val;


    constructor() {
        initialise();
    }


//    function initialise() external {
//        AdminVoting.initialise();
//    }

    /************************************* INTERNAL FUNCTIONS BELOW HERE *************************************/
    /************************************* INTERNAL FUNCTIONS BELOW HERE *************************************/
    /************************************* INTERNAL FUNCTIONS BELOW HERE *************************************/

    function proposeAppVote(uint16 _action, address _voteTarget, uint256 /* _additionalInfo1 */) internal pure override {
        if (_action == VOTE_VAL) {
            require(_voteTarget == address(0), "AppVote: vote target must be zero when changing val");
        }
        else {
            revert("AppVote: Unsupported vote action");
        }
    }
    function actionAppVote(uint16 _action, address _voteTarget, uint256 /* _additionalInfo1 */) internal override {
        if (_action == VOTE_VAL) {
            val = votes[_voteTarget].additionalInfo1;
        }
    }

}
