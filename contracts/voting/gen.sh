#!/usr/bin/env bash
set -e
rm -rf build

HERE=contracts/voting
BUILDDIR=$HERE/build
CONTRACTSDIR=$HERE/src/main/solidity
TESTCONTRACTSDIR=$HERE/src/test/solidity
OUTPUTDIR=$HERE/src/main/java
TESTOUTPUTDIR=$HERE/src/test/java
PACKAGE=net.consensys.htlcbridge.voting.soliditywrappers
WEB3J=web3j


# compiling one file also compiles its dependendencies. We use overwrite to avoid the related warnings.
solc $CONTRACTSDIR/VotingAlgMajority.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
solc $CONTRACTSDIR/VotingAlgMajorityWhoVoted.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
solc $TESTCONTRACTSDIR/VotingTest.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
# ls -al $BUILDDIR

$WEB3J solidity generate -a=$BUILDDIR/VotingAlgMajority.abi -b=$BUILDDIR/VotingAlgMajority.bin -o=$OUTPUTDIR -p=$PACKAGE
$WEB3J solidity generate -a=$BUILDDIR/VotingAlgMajorityWhoVoted.abi -b=$BUILDDIR/VotingAlgMajorityWhoVoted.bin -o=$OUTPUTDIR -p=$PACKAGE
$WEB3J solidity generate -a=$BUILDDIR/VotingTest.abi -b=$BUILDDIR/VotingTest.bin -o=$TESTOUTPUTDIR -p=$PACKAGE


