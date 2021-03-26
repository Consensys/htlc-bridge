package net.consensys.htlcbridge.voting.soliditywrappers;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.16.
 */
@SuppressWarnings("rawtypes")
public class VotingAlgMajority extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b506100216319ab36c160e01b610026565b6100a9565b6001600160e01b031980821614156100845760405162461bcd60e51b815260206004820152601c60248201527f4552433136353a20696e76616c696420696e7465726661636520696400000000604482015260640160405180910390fd5b6001600160e01b0319166000908152602081905260409020805460ff19166001179055565b6102b5806100b86000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c806301ffc9a71461003b57806319ab36c114610062575b600080fd5b61004e610049366004610196565b610075565b604051901515815260200160405180910390f35b61004e6100703660046101c5565b6100b7565b60006301ffc9a760e01b6001600160e01b0319831614806100af57506001600160e01b0319821660009081526020819052604090205460ff165b90505b919050565b60008367ffffffffffffffff16835160026100d2919061023e565b11949350505050565b80356001600160a01b03811681146100b257600080fd5b600082601f830112610102578081fd5b8135602067ffffffffffffffff8083111561011f5761011f610269565b818302604051601f19603f8301168101818110848211171561014357610143610269565b60405284815283810192508684018288018501891015610161578687fd5b8692505b8583101561018a57610176816100db565b845292840192600192909201918401610165565b50979650505050505050565b6000602082840312156101a7578081fd5b81356001600160e01b0319811681146101be578182fd5b9392505050565b6000806000606084860312156101d9578182fd5b833567ffffffffffffffff80821682146101f1578384fd5b90935060208501359080821115610206578384fd5b610212878388016100f2565b93506040860135915080821115610227578283fd5b50610234868287016100f2565b9150509250925092565b600081600019048311821515161561026457634e487b7160e01b81526011600452602481fd5b500290565b634e487b7160e01b600052604160045260246000fdfea2646970667358221220774e0b1dfce67344fcc90f8187de55abca1f1fb87bc8a72f824baa2479b578ae64736f6c63430008020033";

    public static final String FUNC_ASSESS = "assess";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    @Deprecated
    protected VotingAlgMajority(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected VotingAlgMajority(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected VotingAlgMajority(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected VotingAlgMajority(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Boolean> assess(BigInteger numParticipants, List<String> votedFor, List<String> param2) {
        final Function function = new Function(FUNC_ASSESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint64(numParticipants), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(votedFor, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(param2, org.web3j.abi.datatypes.Address.class))), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> supportsInterface(byte[] interfaceId) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static VotingAlgMajority load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new VotingAlgMajority(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static VotingAlgMajority load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new VotingAlgMajority(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static VotingAlgMajority load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new VotingAlgMajority(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static VotingAlgMajority load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new VotingAlgMajority(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<VotingAlgMajority> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VotingAlgMajority.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<VotingAlgMajority> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VotingAlgMajority.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<VotingAlgMajority> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VotingAlgMajority.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<VotingAlgMajority> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VotingAlgMajority.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
