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
public class VotingAlgMajorityWhoVoted extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b506100216319ab36c160e01b610026565b6100a9565b6001600160e01b031980821614156100845760405162461bcd60e51b815260206004820152601c60248201527f4552433136353a20696e76616c696420696e7465726661636520696400000000604482015260640160405180910390fd5b6001600160e01b0319166000908152602081905260409020805460ff19166001179055565b61026a806100b86000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c806301ffc9a71461003b57806319ab36c114610062575b600080fd5b61004e610049366004610176565b610079565b604051901515815260200160405180910390f35b61004e6100703660046101a5565b51905111919050565b60006301ffc9a760e01b6001600160e01b0319831614806100b357506001600160e01b0319821660009081526020819052604090205460ff165b90505b919050565b80356001600160a01b03811681146100b657600080fd5b600082601f8301126100e2578081fd5b8135602067ffffffffffffffff808311156100ff576100ff61021e565b818302604051601f19603f830116810181811084821117156101235761012361021e565b60405284815283810192508684018288018501891015610141578687fd5b8692505b8583101561016a57610156816100bb565b845292840192600192909201918401610145565b50979650505050505050565b600060208284031215610187578081fd5b81356001600160e01b03198116811461019e578182fd5b9392505050565b6000806000606084860312156101b9578182fd5b833567ffffffffffffffff80821682146101d1578384fd5b909350602085013590808211156101e6578384fd5b6101f2878388016100d2565b93506040860135915080821115610207578283fd5b50610214868287016100d2565b9150509250925092565b634e487b7160e01b600052604160045260246000fdfea2646970667358221220518a2ea4ef74f04d3d2f6e31c13bc694522a9e5cea4e297399c7cf5b92459eaf64736f6c63430008020033";

    public static final String FUNC_ASSESS = "assess";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    @Deprecated
    protected VotingAlgMajorityWhoVoted(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected VotingAlgMajorityWhoVoted(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected VotingAlgMajorityWhoVoted(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected VotingAlgMajorityWhoVoted(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Boolean> assess(BigInteger param0, List<String> votedFor, List<String> votedAgainst) {
        final Function function = new Function(FUNC_ASSESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint64(param0), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(votedFor, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(votedAgainst, org.web3j.abi.datatypes.Address.class))), 
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
    public static VotingAlgMajorityWhoVoted load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new VotingAlgMajorityWhoVoted(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static VotingAlgMajorityWhoVoted load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new VotingAlgMajorityWhoVoted(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static VotingAlgMajorityWhoVoted load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new VotingAlgMajorityWhoVoted(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static VotingAlgMajorityWhoVoted load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new VotingAlgMajorityWhoVoted(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<VotingAlgMajorityWhoVoted> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VotingAlgMajorityWhoVoted.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<VotingAlgMajorityWhoVoted> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VotingAlgMajorityWhoVoted.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<VotingAlgMajorityWhoVoted> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VotingAlgMajorityWhoVoted.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<VotingAlgMajorityWhoVoted> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VotingAlgMajorityWhoVoted.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
