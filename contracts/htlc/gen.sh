#!/usr/bin/env bash
set -e
rm -rf build

HERE=contracts/htlc
BUILDDIR=$HERE/build
CONTRACTSDIR=$HERE/src/main/solidity
TESTCONTRACTSDIR=$HERE/src/test/solidity
OUTPUTDIR=$HERE/src/main/java
TESTOUTPUTDIR=$HERE/src/test/java
PACKAGE=net.consensys.htlcbridge.transfer.soliditywrappers
WEB3J=web3j
#WEB3J=../web3j-rlp/codegen/build/distributions/codegen-4.7.0-SNAPSHOT/bin/codegen


# compiling one file also compiles its dependendencies. We use overwrite to avoid the related warnings.
solc $CONTRACTSDIR/Erc20HtlcTransfer.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
#solc $TESTCONTRACTSDIR/Erc20HtlcTransferTest.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
# ls -al $BUILDDIR

$WEB3J solidity generate -a=$BUILDDIR/Erc20HtlcTransfer.abi -b=$BUILDDIR/Erc20HtlcTransfer.bin -o=$OUTPUTDIR -p=$PACKAGE
#$WEB3J solidity generate -a=$BUILDDIR/TestLockableStorageWrapper.abi -b=$BUILDDIR/TestLockableStorageWrapper.bin -o=$TESTOUTPUTDIR -p=$PACKAGE


