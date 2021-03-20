#!/usr/bin/env bash
set -e
rm -rf build

HERE=contracts/openzeppelin
BUILDDIR=$HERE/build
CONTRACTSDIR=$HERE/src/main/solidity
TESTCONTRACTSDIR=$HERE/src/test/solidity
OUTPUTDIR=$HERE/src/main/java
TESTOUTPUTDIR=$HERE/src/test/java
PACKAGE=net.consensys.htlcbridge.openzeppelin.soliditywrappers
WEB3J=web3j
#WEB3J=../web3j-rlp/codegen/build/distributions/codegen-4.7.0-SNAPSHOT/bin/codegen


# compiling one file also compiles its dependendencies. We use overwrite to avoid the related warnings.
solc $CONTRACTSDIR/token/ERC20/presets/ERC20PresetFixedSupply.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
#solc $TESTCONTRACTSDIR/Erc20HtlcTransferTest.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
# ls -al $BUILDDIR

$WEB3J solidity generate -a=$BUILDDIR/ERC20PresetFixedSupply.abi -b=$BUILDDIR/ERC20PresetFixedSupply.bin -o=$OUTPUTDIR -p=$PACKAGE
#$WEB3J solidity generate -a=$BUILDDIR/LockableStorage.abi -b=$BUILDDIR/LockableStorage.bin -o=$OUTPUTDIR -p=$PACKAGE
#$WEB3J solidity generate -a=$BUILDDIR/MockCbcForLockableStorageTest.abi -b=$BUILDDIR/MockCbcForLockableStorageTest.bin -o=$TESTOUTPUTDIR -p=$PACKAGE
#$WEB3J solidity generate -a=$BUILDDIR/TestLockableStorageWrapper.abi -b=$BUILDDIR/TestLockableStorageWrapper.bin -o=$TESTOUTPUTDIR -p=$PACKAGE


