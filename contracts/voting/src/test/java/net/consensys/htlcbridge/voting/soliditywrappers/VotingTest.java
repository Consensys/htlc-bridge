package net.consensys.htlcbridge.voting.soliditywrappers;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint16;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
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
public class VotingTest extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50610046336000908152600360205260409020805460ff19166001908117909155600280546001600160401b0319169091179055565b6114b0806100556000396000f3fe608060405234801561001057600080fd5b50600436106100b45760003560e01c8063633858f311610071578063633858f314610171578063721c3c64146101975780637bd7f87c146101c7578063abc6814e146101da578063b2e4fece14610208578063d69d6e6f14610234576100b4565b8063079cf38a146100b9578063181d15ba146100df57806323113b76146100f457806324d7806c1461011f57806353f598b914610142578063581cb2dc14610155575b600080fd5b6100cc6100c736600461120a565b610247565b6040519081526020015b60405180910390f35b6100f26100ed3660046112e0565b610295565b005b610107610102366004611240565b6106f7565b6040516001600160a01b0390911681526020016100d6565b61013261012d3660046111f0565b610798565b60405190151581526020016100d6565b6100f26101503660046111f0565b6107ba565b60025460405167ffffffffffffffff90911681526020016100d6565b61018461017f3660046111f0565b6109e5565b60405161ffff90911681526020016100d6565b600154604080516001600160a01b0383168152600160a01b90920467ffffffffffffffff166020830152016100d6565b6101326101d53660046111f0565b610a20565b6101326101e83660046111f0565b6001600160a01b0316600090815260208190526040902060020154421190565b6100cc6102163660046111f0565b6001600160a01b031660009081526020819052604090206002015490565b6100f261024236600461129a565b610a66565b6000811561027157506001600160a01b03821660009081526020819052604090206004015461028f565b506001600160a01b0382166000908152602081905260409020600501545b92915050565b3360009081526003602052604090205460ff166102cd5760405162461bcd60e51b81526004016102c490611361565b60405180910390fd5b6102d682610a20565b156103235760405162461bcd60e51b815260206004820152601a60248201527f566f74696e6720616c726561647920696e2070726f677265737300000000000060448201526064016102c4565b60008361ffff16600381111561034957634e487b7160e01b600052602160045260246000fd5b9050600181600381111561036d57634e487b7160e01b600052602160045260246000fd5b14156103e05761037c83610798565b156103db5760405162461bcd60e51b815260206004820152602960248201527f566f746541646441646d696e3a204164647265737320697320616c72656164796044820152681030b71030b236b4b760b91b60648201526084016102c4565b61063f565b600281600381111561040257634e487b7160e01b600052602160045260246000fd5b14156104d35761041183610798565b61046e5760405162461bcd60e51b815260206004820152602860248201527f566f746552656d6f766541646d696e3a2041646472657373206973206e6f742060448201526730b71030b236b4b760c11b60648201526084016102c4565b6001600160a01b0383163314156103db5760405162461bcd60e51b8152602060048201526024808201527f566f746552656d6f766541646d696e3a2043616e206e6f742072656d6f76652060448201526339b2b63360e11b60648201526084016102c4565b60038160038111156104f557634e487b7160e01b600052602160045260246000fd5b141561063f576040516301ffc9a760e01b81526319ab36c160e01b600482015283906001600160a01b038216906301ffc9a79060240160206040518083038186803b15801561054357600080fd5b505afa158015610557573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061057b919061127e565b6105ed5760405162461bcd60e51b815260206004820152603960248201527f566f74654368616e6765566f74696e673a2050726f706f73656420636f6e747260448201527f616374206e6f74206120766f74696e6720636f6e74726163740000000000000060648201526084016102c4565b6000831161063d5760405162461bcd60e51b815260206004820152601b60248201527f50726f706f73656420766f74696e6720706572696f642069732030000000000060448201526064016102c4565b505b6001600160a01b0383166000908152602081905260409020805482919060ff1916600183600381111561068257634e487b7160e01b600052602160045260246000fd5b02179055506001546106a590600160a01b900467ffffffffffffffff16426113d7565b6001600160a01b038085166000908152602081905260409020600281019290925560019182018490559054166106e5576106e0836001610cac565b6106f1565b6106f184846001611088565b50505050565b60008215610756576001600160a01b038416600090815260208190526040902060040180548390811061073a57634e487b7160e01b600052603260045260246000fd5b6000918252602090912001546001600160a01b03169050610791565b6001600160a01b038416600090815260208190526040902060050180548390811061073a57634e487b7160e01b600052603260045260246000fd5b9392505050565b6001600160a01b03811660009081526003602052604090205460ff165b919050565b3360009081526003602052604090205460ff166107e95760405162461bcd60e51b81526004016102c490611361565b6107f281610a20565b6108305760405162461bcd60e51b815260206004820152600f60248201526e566f7465206e6f742061637469766560881b60448201526064016102c4565b6001600160a01b03811660009081526020819052604090206002015442116108a45760405162461bcd60e51b815260206004820152602160248201527f566f74696e6720706572696f6420686173206e6f7420796574206578706972656044820152601960fa1b60648201526084016102c4565b6001546002546001600160a01b0383811660009081526020819052604080822090516319ab36c160e01b81529290941693909284926319ab36c1926108fe9267ffffffffffffffff16916004808301926005019101611398565b60206040518083038186803b15801561091657600080fd5b505afa15801561092a573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061094e919061127e565b6001600160a01b03841660009081526020819052604090205490915060ff167f9d4ce721e9765ae20e979179e0edb2b4d216041d2824a032c04687c7f0da764d8160038111156109ae57634e487b7160e01b600052602160045260246000fd5b6040805161ffff90921682526001600160a01b03871660208301528415159082015260600160405180910390a16106f18483610cac565b6001600160a01b03811660009081526020819052604081205460ff16600381111561028f57634e487b7160e01b600052602160045260246000fd5b6000806001600160a01b03831660009081526020819052604090205460ff166003811115610a5e57634e487b7160e01b600052602160045260246000fd5b141592915050565b3360009081526003602052604090205460ff16610a955760405162461bcd60e51b81526004016102c490611361565b610a9e82610a20565b610adc5760405162461bcd60e51b815260206004820152600f60248201526e566f7465206e6f742061637469766560881b60448201526064016102c4565b6001600160a01b038216600090815260208190526040902060020154421115610b475760405162461bcd60e51b815260206004820152601960248201527f566f74696e6720706572696f642068617320657870697265640000000000000060448201526064016102c4565b6001600160a01b03821660009081526020818152604080832033845260030190915290205460ff1615610bbc5760405162461bcd60e51b815260206004820152601960248201527f4163636f756e742068617320616c726561647920766f7465640000000000000060448201526064016102c4565b60008361ffff166003811115610be257634e487b7160e01b600052602160045260246000fd5b9050806003811115610c0457634e487b7160e01b600052602160045260246000fd5b6001600160a01b03841660009081526020819052604090205460ff166003811115610c3f57634e487b7160e01b600052602160045260246000fd5b14610ca15760405162461bcd60e51b815260206004820152602c60248201527f566f74696e6720616374696f6e20646f6573206e6f74206d617463682061637460448201526b1a5d99481c1c9bdc1bdcd85b60a21b60648201526084016102c4565b6106f1848484611088565b8015610e2c576001600160a01b0382166000908152602081905260409020805460019182015460ff90911691826003811115610cf857634e487b7160e01b600052602160045260246000fd5b1415610d65576001600160a01b0384166000908152600360205260408120805460ff191660011790556002805467ffffffffffffffff1691610d398361142e565b91906101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555050610e29565b6002826003811115610d8757634e487b7160e01b600052602160045260246000fd5b1415610dc5576001600160a01b0384166000908152600360205260408120805460ff191690556002805467ffffffffffffffff1691610d39836113ef565b6003826003811115610de757634e487b7160e01b600052602160045260246000fd5b1415610e2957600180546001600160a01b0319166001600160a01b0386161767ffffffffffffffff60a01b1916600160a01b67ffffffffffffffff8416021790555b50505b6001600160a01b03821660009081526020818152604080832060040180548251818502810185019093528083529192909190830182828015610e9757602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610e79575b5050505050905060005b8151811015610f3057600080856001600160a01b03166001600160a01b031681526020019081526020016000206003016000838381518110610ef357634e487b7160e01b600052603260045260246000fd5b6020908102919091018101516001600160a01b03168252810191909152604001600020805460ff1916905580610f2881611413565b915050610ea1565b506001600160a01b03831660009081526020818152604080832060050180548251818502810185019093528083529192909190830182828015610f9c57602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610f7e575b5050505050905060005b815181101561103557600080866001600160a01b03166001600160a01b031681526020019081526020016000206003016000838381518110610ff857634e487b7160e01b600052603260045260246000fd5b6020908102919091018101516001600160a01b03168252810191909152604001600020805460ff191690558061102d81611413565b915050610fa6565b506001600160a01b0384166000908152602081905260408120805460ff19168155600181018290556002810182905590611072600483018261118d565b61108060058301600061118d565b505050505050565b6040805133815261ffff851660208201526001600160a01b03841681830152821515606082015290517f32b8c3a9535767c48b7f4575cbd5402f63b03ba60ee9767254ee701a41123b069181900360800190a16001600160a01b0382166000908152602081815260408083203384526003019091529020805460ff19166001179055801561114e576001600160a01b0382166000908152602081815260408220600401805460018101825590835291200180546001600160a01b03191633179055611188565b6001600160a01b0382166000908152602081815260408220600501805460018101825590835291200180546001600160a01b031916331790555b505050565b50805460008255906000526020600020908101906111ab91906111ae565b50565b5b808211156111c357600081556001016111af565b5090565b80356001600160a01b03811681146107b557600080fd5b803561ffff811681146107b557600080fd5b600060208284031215611201578081fd5b610791826111c7565b6000806040838503121561121c578081fd5b611225836111c7565b915060208301356112358161146c565b809150509250929050565b600080600060608486031215611254578081fd5b61125d846111c7565b9250602084013561126d8161146c565b929592945050506040919091013590565b60006020828403121561128f578081fd5b81516107918161146c565b6000806000606084860312156112ae578283fd5b6112b7846111de565b92506112c5602085016111c7565b915060408401356112d58161146c565b809150509250925092565b6000806000606084860312156112f4578283fd5b6112fd846111de565b925061130b602085016111c7565b9150604084013590509250925092565b6000815480845260208085019450838352808320835b838110156113565781546001600160a01b031687529582019560019182019101611331565b509495945050505050565b6020808252601a908201527f6d73672e73656e646572206973206e6f7420616e2061646d696e000000000000604082015260600190565b600067ffffffffffffffff85168252606060208301526113bb606083018561131b565b82810360408401526113cd818561131b565b9695505050505050565b600082198211156113ea576113ea611456565b500190565b600067ffffffffffffffff82168061140957611409611456565b6000190192915050565b600060001982141561142757611427611456565b5060010190565b600067ffffffffffffffff8083168181141561144c5761144c611456565b6001019392505050565b634e487b7160e01b600052601160045260246000fd5b80151581146111ab57600080fdfea2646970667358221220c45aa738984bf1fc4fb7ad86fcd57ad6ded716dcbe0c6c3d78ea59a786ca712664736f6c63430008020033";

    public static final String FUNC_ACTIONVOTES = "actionVotes";

    public static final String FUNC_ENDOFVOTINGPERIOD = "endOfVotingPeriod";

    public static final String FUNC_GETNUMADMINS = "getNumAdmins";

    public static final String FUNC_GETVOTINGCONFIG = "getVotingConfig";

    public static final String FUNC_ISADMIN = "isAdmin";

    public static final String FUNC_ISVOTEACTIVE = "isVoteActive";

    public static final String FUNC_NUMVOTES = "numVotes";

    public static final String FUNC_PROPOSEVOTE = "proposeVote";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_VOTEPERIODEXPIRED = "votePeriodExpired";

    public static final String FUNC_VOTETYPE = "voteType";

    public static final String FUNC_WHOVOTED = "whoVoted";

    public static final Event VOTERESULT_EVENT = new Event("VoteResult", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint16>() {}, new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
    ;

    public static final Event VOTED_EVENT = new Event("Voted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint16>() {}, new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
    ;

    @Deprecated
    protected VotingTest(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected VotingTest(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected VotingTest(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected VotingTest(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<VoteResultEventResponse> getVoteResultEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(VOTERESULT_EVENT, transactionReceipt);
        ArrayList<VoteResultEventResponse> responses = new ArrayList<VoteResultEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VoteResultEventResponse typedResponse = new VoteResultEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._action = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._voteTarget = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._result = (Boolean) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VoteResultEventResponse> voteResultEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, VoteResultEventResponse>() {
            @Override
            public VoteResultEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTERESULT_EVENT, log);
                VoteResultEventResponse typedResponse = new VoteResultEventResponse();
                typedResponse.log = log;
                typedResponse._action = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._voteTarget = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._result = (Boolean) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VoteResultEventResponse> voteResultEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTERESULT_EVENT));
        return voteResultEventFlowable(filter);
    }

    public List<VotedEventResponse> getVotedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(VOTED_EVENT, transactionReceipt);
        ArrayList<VotedEventResponse> responses = new ArrayList<VotedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VotedEventResponse typedResponse = new VotedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._participant = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._action = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._voteTarget = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._votedFor = (Boolean) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VotedEventResponse> votedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, VotedEventResponse>() {
            @Override
            public VotedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTED_EVENT, log);
                VotedEventResponse typedResponse = new VotedEventResponse();
                typedResponse.log = log;
                typedResponse._participant = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._action = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._voteTarget = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._votedFor = (Boolean) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VotedEventResponse> votedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTED_EVENT));
        return votedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> actionVotes(String _voteTarget) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ACTIONVOTES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _voteTarget)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> endOfVotingPeriod(String _voteTarget) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ENDOFVOTINGPERIOD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _voteTarget)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getNumAdmins() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETNUMADMINS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple2<String, BigInteger>> getVotingConfig() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETVOTINGCONFIG, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint64>() {}));
        return new RemoteFunctionCall<Tuple2<String, BigInteger>>(function,
                new Callable<Tuple2<String, BigInteger>>() {
                    @Override
                    public Tuple2<String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Boolean> isAdmin(String _mightBeAdmin) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _mightBeAdmin)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isVoteActive(String _voteTarget) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISVOTEACTIVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _voteTarget)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> numVotes(String _voteTarget, Boolean _votedFor) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NUMVOTES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _voteTarget), 
                new org.web3j.abi.datatypes.Bool(_votedFor)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> proposeVote(BigInteger _action, String _voteTarget, BigInteger _additionalInfo1) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_PROPOSEVOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint16(_action), 
                new org.web3j.abi.datatypes.Address(160, _voteTarget), 
                new org.web3j.abi.datatypes.generated.Uint256(_additionalInfo1)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> vote(BigInteger _action, String _voteTarget, Boolean _voteFor) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_VOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint16(_action), 
                new org.web3j.abi.datatypes.Address(160, _voteTarget), 
                new org.web3j.abi.datatypes.Bool(_voteFor)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> votePeriodExpired(String _voteTarget) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_VOTEPERIODEXPIRED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _voteTarget)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> voteType(String _voteTarget) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_VOTETYPE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _voteTarget)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint16>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> whoVoted(String _voteTarget, Boolean _votedFor, BigInteger _index) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_WHOVOTED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _voteTarget), 
                new org.web3j.abi.datatypes.Bool(_votedFor), 
                new org.web3j.abi.datatypes.generated.Uint256(_index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static VotingTest load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new VotingTest(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static VotingTest load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new VotingTest(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static VotingTest load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new VotingTest(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static VotingTest load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new VotingTest(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<VotingTest> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VotingTest.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<VotingTest> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VotingTest.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<VotingTest> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VotingTest.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<VotingTest> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VotingTest.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class VoteResultEventResponse extends BaseEventResponse {
        public BigInteger _action;

        public String _voteTarget;

        public Boolean _result;
    }

    public static class VotedEventResponse extends BaseEventResponse {
        public String _participant;

        public BigInteger _action;

        public String _voteTarget;

        public Boolean _votedFor;
    }
}
