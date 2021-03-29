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

import "./VotingAlgInterface.sol";

/**
 * Contract to allow administrators to vote on decisions.
 */
abstract contract AdminVoting {
    // Indications that a vote is underway.
    // VOTE_NONE indicates no vote is underway. Also matches the deleted value for integers.
    uint256 constant VOTE_NONE = 0;   // MUST be the first value so it is the zero / deleted value.
    uint256 constant VOTE_ADD_ADMIN = 1;
    uint256 constant VOTE_REMOVE_ADMIN = 2;
    uint256 constant VOTE_CHANGE_VOTING = 3;
    uint256 constant VOTE_APPLICATION_START = 100;


    struct Votes {
        // The type of vote being voted on.
        uint16 voteType;
        // Additional information about what is being voted on.
        uint256 additionalInfo1;
        // The end of the voting period in seconds.
        uint endOfVotingPeriod;

        // Map of who has voted on the proposal.
        mapping(address=>bool) hasVoted;
        // The participants who voted for the proposal.
        address[] votedFor;
        // The participants who voted against the proposal.
        address[] votedAgainst;
    }
    mapping(address=>Votes) private votes;

    // The algorithm for assessing the votes.
    address private votingAlgorithmContract;
    // Voting period in blocks. This is the period in which participants can vote. Must be greater than 0.
    uint64 private votingPeriod;

    // Number of active administrators.
    uint64 private numAdmins;
    // Address of accounts who administer this contract.
    mapping(address => bool) private adminsMap;



    /**
     * Function modifier to ensure only admins can call the function.
     *
     * @dev Throws if the message sender isn't an admin.
     */
    modifier onlyAdmin() {
        require(adminsMap[msg.sender], "msg.sender is not an admin");
        _;
    }


    /**
     * To allow for the proxy pattern, don't have a constructor.
     * See: https://docs.openzeppelin.com/upgrades-plugins/1.x/writing-upgradeable
     *
     */
    function initialise() internal {
        // Have msg.sender deploying this contract as an admin
        adminsMap[msg.sender] = true;
        numAdmins = 1;
    }


    /**
    * Propose that a certain action be voted on. Proposals are actioned immediately if there is no
    * voting algorithm. If there is a voting algorithm, when an account proposes a vote, it
    * automatically votes for the vote. That is, the proposer does not need to separately call the
    * vote function.
    *
    * Types of votes:
    *
    * Value  Action                                  Target                        Additional Information1
    * 1      Add an admin                            Address of admin to add       ignored
    *         Revert if the address is already an admin.
    * 2      Remove an admin                         Address of admin to remove    ignored
    *         Revert if the address is not an admin.
    * 3      Change voting algorithm & voting period Address of voting contract    Voting period
    *         Proposing a voting algorithm with address(0) removes the voting algorithm.
    *         The voting period must be greater than 1. Voting period is in seconds.
    *         NOTE: This value is relative to what is returned by block.timestamp. If block.timestamp
    *         returns values other than seconds, then voting period value need to be scaled to match that.
    *
    *
    * @param _action         The type of vote
    * @param _voteTarget     What is being voted on
    * @param _additionalInfo1 Additional information as per the table above.
    */
    function proposeVote(uint16 _action, address _voteTarget, uint256 _additionalInfo1) external onlyAdmin() {

        // Can't start a vote if a vote is already underway.
        require(!isVoteActive(_voteTarget), "Voting already in progress");

        if (_action == VOTE_NONE) {
            revert("VoteNone: Can not vote for nothing");
        }
        else if (_action == VOTE_ADD_ADMIN) {
            // If the action is to add an admin, then they shouldn't be an admin already.
            require(!isAdmin(_voteTarget), "VoteAddAdmin: Address is already an admin");
        }
        else if (_action == VOTE_REMOVE_ADMIN) {
            // If the action is to remove an admin, then they should be an admin already.
            require(isAdmin(_voteTarget), "VoteRemoveAdmin: Address is not an admin");
            // Don't allow admins to propose removing themselves. This means the case of removing
            // the only admin is avoided.
            require(_voteTarget != msg.sender, "VoteRemoveAdmin: Can not remove self");
        }
        else if (_action == VOTE_CHANGE_VOTING) {
            IERC165 votingAlgContract = IERC165(_voteTarget);
            require(votingAlgContract.supportsInterface(type(VotingAlgInterface).interfaceId),
                "VoteChangeVoting: Proposed contract not a voting contract");
            require(_additionalInfo1 > 0, "Proposed voting period is 0");
        }
        else if (_action < VOTE_APPLICATION_START) {
            revert("VoteAdmin: type not supported");
        }
        else {
            proposeAppVote(_action, _voteTarget, _additionalInfo1);
        }


        // Set-up the vote.
        votes[_voteTarget].voteType = _action;
        votes[_voteTarget].endOfVotingPeriod = block.timestamp + votingPeriod;
        votes[_voteTarget].additionalInfo1 = _additionalInfo1;

        if (votingAlgorithmContract == address(0)) {
            // If there is no voting algorithm then all proposals are acted upon immediately.
            actionVotesNoChecks(_voteTarget, true);
        }
        else {
            // If there is a voting algorithm, then a full vote is required. The proposer is
            // deemed to be voting for the proposal.
            voteNoChecks(_action, _voteTarget, true);
        }
    }

    /**
     * Vote for a proposal.
     *
     * If an account has already voted, they can not vote again or change their vote.
     *
     * @param _action The type of vote.
     * @param _voteTarget What is being voted on
     * @param _voteFor True if the transaction sender wishes to vote for the action.
     */
    function vote(uint16 _action, address _voteTarget, bool _voteFor) external onlyAdmin() {
        require(isVoteActive(_voteTarget), "Vote not active");
        require(!votePeriodExpired(_voteTarget), "Voting period has expired");
        require(votes[_voteTarget].hasVoted[msg.sender] == false, "Account has already voted");

        require(votes[_voteTarget].voteType == _action, "Voting action does not match active proposal");

        voteNoChecks(_action, _voteTarget, _voteFor);
    }

    /**
     * Action votes to affect the change.
     *
     * Only admins can action votes.
     *
     * @param _voteTarget What is being voted on.
     */
    function actionVotes(address _voteTarget) external onlyAdmin() {
        require(isVoteActive(_voteTarget), "Vote not active");
        require(votePeriodExpired(_voteTarget), "Voting period has not yet expired");

        VotingAlgInterface voteAlg = VotingAlgInterface(votingAlgorithmContract);
        bool result = voteAlg.assess(
            numAdmins,
            votes[_voteTarget].votedFor,
            votes[_voteTarget].votedAgainst);
        uint16 action = votes[_voteTarget].voteType;
        emit VoteResult(action, _voteTarget, result);

        actionVotesNoChecks(_voteTarget, result);
    }


    function votePeriodExpired(address _voteTarget) public view returns (bool)  {
        return votes[_voteTarget].endOfVotingPeriod < block.timestamp;
    }

    function endOfVotingPeriod(address _voteTarget) public view returns (uint256)  {
        return votes[_voteTarget].endOfVotingPeriod;
    }

    function isVoteActive(address _voteTarget) public view returns (bool)  {
        return votes[_voteTarget].voteType != VOTE_NONE;
    }

    function voteType(address _voteTarget) external view returns (uint16)  {
        return votes[_voteTarget].voteType;
    }

    function isAdmin(address _mightBeAdmin) public view returns (bool)  {
        return adminsMap[_mightBeAdmin];
    }

    function getNumAdmins() external view returns (uint64) {
        return numAdmins;
    }

    function getVotingConfig() external view returns (address, uint64) {
        return (votingAlgorithmContract, votingPeriod);
    }

    /**
     * Observing the voting events will show who has voted. This function can be used
     * as an alternative mechanism while a vote is active.
     */
    function whoVoted(address _voteTarget, bool _votedFor, uint256 _index) public view returns (address)  {
        if (_votedFor) {
            return votes[_voteTarget].votedFor[_index];
        }
        else {
            return votes[_voteTarget].votedAgainst[_index];
        }
    }

    function numVotes(address _voteTarget, bool _votedFor) public view returns (uint256)  {
        if (_votedFor) {
            return votes[_voteTarget].votedFor.length;
        }
        else {
            return votes[_voteTarget].votedAgainst.length;
        }
    }

    /************************************* INTERNAL FUNCTIONS BELOW HERE *************************************/
    /************************************* INTERNAL FUNCTIONS BELOW HERE *************************************/
    /************************************* INTERNAL FUNCTIONS BELOW HERE *************************************/

    // Code that extends this contract must implement these functions to implement their own actions.
    function proposeAppVote(uint16 _action, address _voteTarget, uint256 _additionalInfo1) internal virtual {}
    function actionAppVote(uint16 _action, address _voteTarget, uint256 _additionalInfo1) internal virtual {}


    /************************************* PRIVATE FUNCTIONS BELOW HERE *************************************/
    /************************************* PRIVATE FUNCTIONS BELOW HERE *************************************/
    /************************************* PRIVATE FUNCTIONS BELOW HERE *************************************/

    function voteNoChecks(uint16 _action, address _voteTarget, bool _voteFor) private {
        // Indicate msg.sender has voted.
        emit Voted(msg.sender, _action, _voteTarget, _voteFor);
        votes[_voteTarget].hasVoted[msg.sender] = true;

        if (_voteFor) {
            votes[_voteTarget].votedFor.push(msg.sender);
        } else {
            votes[_voteTarget].votedAgainst.push(msg.sender);
        }
    }


    function actionVotesNoChecks(address _voteTarget, bool _result) private {
        if (_result) {
            // The vote has been decided in the affirmative.
            uint16 action = votes[_voteTarget].voteType;
            uint256 additionalInfo1 = votes[_voteTarget].additionalInfo1;
            if (action == VOTE_ADD_ADMIN) {
                adminsMap[_voteTarget] = true;
                numAdmins++;
            }
            else if (action == VOTE_REMOVE_ADMIN) {
                delete adminsMap[_voteTarget];
                numAdmins--;
            }
            else if (action == VOTE_CHANGE_VOTING) {
                votingAlgorithmContract = _voteTarget;
                votingPeriod = uint64(additionalInfo1);
            }
            else {
                actionAppVote(action, _voteTarget, additionalInfo1);
            }

        }


        // The vote is over. Now delete the voting arrays and indicate there is no vote underway.
        address[] memory votedFor = votes[_voteTarget].votedFor;
        for (uint i = 0; i < votedFor.length; i++) {
            delete votes[_voteTarget].hasVoted[votedFor[i]];
        }
        address[] memory votedAgainst = votes[_voteTarget].votedAgainst;
        for (uint i = 0; i < votedAgainst.length; i++) {
            delete votes[_voteTarget].hasVoted[votedAgainst[i]];
        }
        // This will recursively delete everything in the structure, except for the map, which was
        // deleted in the for loop above.
        delete votes[_voteTarget];
    }



    event Voted(address _participant, uint16 _action, address _voteTarget, bool _votedFor);
    event VoteResult(uint16 _action, address _voteTarget, bool _result);

}
