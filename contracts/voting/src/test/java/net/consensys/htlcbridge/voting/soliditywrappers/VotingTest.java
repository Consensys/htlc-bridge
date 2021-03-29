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
    public static final String BINARY = "608060405234801561001057600080fd5b50610046336000908152600360205260409020805460ff19166001908117909155600280546001600160401b0319169091179055565b6113ab806100556000396000f3fe608060405234801561001057600080fd5b50600436106100b45760003560e01c8063633858f311610071578063633858f314610171578063721c3c64146101b15780637bd7f87c146101e1578063abc6814e146101f4578063b2e4fece14610222578063d69d6e6f1461024e576100b4565b8063079cf38a146100b9578063181d15ba146100df57806323113b76146100f457806324d7806c1461011f57806353f598b914610142578063581cb2dc14610155575b600080fd5b6100cc6100c7366004611105565b610261565b6040519081526020015b60405180910390f35b6100f26100ed3660046111db565b6102af565b005b61010761010236600461113b565b61072b565b6040516001600160a01b0390911681526020016100d6565b61013261012d3660046110eb565b6107cc565b60405190151581526020016100d6565b6100f26101503660046110eb565b6107ee565b60025460405167ffffffffffffffff90911681526020016100d6565b61019e61017f3660046110eb565b6001600160a01b031660009081526020819052604090205461ffff1690565b60405161ffff90911681526020016100d6565b600154604080516001600160a01b0383168152600160a01b90920467ffffffffffffffff166020830152016100d6565b6101326101ef3660046110eb565b6109f6565b6101326102023660046110eb565b6001600160a01b0316600090815260208190526040902060020154421190565b6100cc6102303660046110eb565b6001600160a01b031660009081526020819052604090206002015490565b6100f261025c366004611195565b610a17565b6000811561028b57506001600160a01b0382166000908152602081905260409020600401546102a9565b506001600160a01b0382166000908152602081905260409020600501545b92915050565b3360009081526003602052604090205460ff166102e75760405162461bcd60e51b81526004016102de9061125c565b60405180910390fd5b6102f0826109f6565b1561033d5760405162461bcd60e51b815260206004820152601a60248201527f566f74696e6720616c726561647920696e2070726f677265737300000000000060448201526064016102de565b61ffff83166103995760405162461bcd60e51b815260206004820152602260248201527f566f74654e6f6e653a2043616e206e6f7420766f746520666f72206e6f7468696044820152616e6760f01b60648201526084016102de565b60018361ffff161415610413576103af826107cc565b1561040e5760405162461bcd60e51b815260206004820152602960248201527f566f746541646441646d696e3a204164647265737320697320616c72656164796044820152681030b71030b236b4b760b91b60648201526084016102de565b610695565b60028361ffff1614156104eb57610429826107cc565b6104865760405162461bcd60e51b815260206004820152602860248201527f566f746552656d6f766541646d696e3a2041646472657373206973206e6f742060448201526730b71030b236b4b760c11b60648201526084016102de565b6001600160a01b03821633141561040e5760405162461bcd60e51b8152602060048201526024808201527f566f746552656d6f766541646d696e3a2043616e206e6f742072656d6f76652060448201526339b2b63360e11b60648201526084016102de565b60038361ffff161415610640576040516301ffc9a760e01b81526319ab36c160e01b600482015282906001600160a01b038216906301ffc9a79060240160206040518083038186803b15801561054057600080fd5b505afa158015610554573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906105789190611179565b6105ea5760405162461bcd60e51b815260206004820152603960248201527f566f74654368616e6765566f74696e673a2050726f706f73656420636f6e747260448201527f616374206e6f74206120766f74696e6720636f6e74726163740000000000000060648201526084016102de565b6000821161063a5760405162461bcd60e51b815260206004820152601b60248201527f50726f706f73656420766f74696e6720706572696f642069732030000000000060448201526064016102de565b50610695565b60648361ffff1610156106955760405162461bcd60e51b815260206004820152601d60248201527f566f746541646d696e3a2074797065206e6f7420737570706f7274656400000060448201526064016102de565b6001600160a01b0382166000908152602081905260409020805461ffff191661ffff85161790556001546106da90600160a01b900467ffffffffffffffff16426112d2565b6001600160a01b0380841660009081526020819052604090206002810192909255600191820183905590541661071a57610715826001610bfb565b610726565b61072683836001610f84565b505050565b6000821561078a576001600160a01b038416600090815260208190526040902060040180548390811061076e57634e487b7160e01b600052603260045260246000fd5b6000918252602090912001546001600160a01b031690506107c5565b6001600160a01b038416600090815260208190526040902060050180548390811061076e57634e487b7160e01b600052603260045260246000fd5b9392505050565b6001600160a01b03811660009081526003602052604090205460ff165b919050565b3360009081526003602052604090205460ff1661081d5760405162461bcd60e51b81526004016102de9061125c565b610826816109f6565b6108645760405162461bcd60e51b815260206004820152600f60248201526e566f7465206e6f742061637469766560881b60448201526064016102de565b6001600160a01b03811660009081526020819052604090206002015442116108d85760405162461bcd60e51b815260206004820152602160248201527f566f74696e6720706572696f6420686173206e6f7420796574206578706972656044820152601960fa1b60648201526084016102de565b6001546002546001600160a01b0383811660009081526020819052604080822090516319ab36c160e01b81529290941693909284926319ab36c1926109329267ffffffffffffffff16916004808301926005019101611293565b60206040518083038186803b15801561094a57600080fd5b505afa15801561095e573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906109829190611179565b6001600160a01b0384166000818152602081815260409182902054825161ffff90911680825291810193909352831515838301529051929350917f9d4ce721e9765ae20e979179e0edb2b4d216041d2824a032c04687c7f0da764d9181900360600190a16109f08483610bfb565b50505050565b6001600160a01b031660009081526020819052604090205461ffff16151590565b3360009081526003602052604090205460ff16610a465760405162461bcd60e51b81526004016102de9061125c565b610a4f826109f6565b610a8d5760405162461bcd60e51b815260206004820152600f60248201526e566f7465206e6f742061637469766560881b60448201526064016102de565b6001600160a01b038216600090815260208190526040902060020154421115610af85760405162461bcd60e51b815260206004820152601960248201527f566f74696e6720706572696f642068617320657870697265640000000000000060448201526064016102de565b6001600160a01b03821660009081526020818152604080832033845260030190915290205460ff1615610b6d5760405162461bcd60e51b815260206004820152601960248201527f4163636f756e742068617320616c726561647920766f7465640000000000000060448201526064016102de565b6001600160a01b03821660009081526020819052604090205461ffff848116911614610bf05760405162461bcd60e51b815260206004820152602c60248201527f566f74696e6720616374696f6e20646f6573206e6f74206d617463682061637460448201526b1a5d99481c1c9bdc1bdcd85b60a21b60648201526084016102de565b610726838383610f84565b8015610d27576001600160a01b0382166000908152602081905260409020805460019182015461ffff90911691821415610c96576001600160a01b0384166000908152600360205260408120805460ff191660011790556002805467ffffffffffffffff1691610c6a83611329565b91906101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555050610d24565b60028261ffff161415610cdb576001600160a01b0384166000908152600360205260408120805460ff191690556002805467ffffffffffffffff1691610c6a836112ea565b60038261ffff161415610d2457600180546001600160a01b0319166001600160a01b0386161767ffffffffffffffff60a01b1916600160a01b67ffffffffffffffff8416021790555b50505b6001600160a01b03821660009081526020818152604080832060040180548251818502810185019093528083529192909190830182828015610d9257602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610d74575b5050505050905060005b8151811015610e2b57600080856001600160a01b03166001600160a01b031681526020019081526020016000206003016000838381518110610dee57634e487b7160e01b600052603260045260246000fd5b6020908102919091018101516001600160a01b03168252810191909152604001600020805460ff1916905580610e238161130e565b915050610d9c565b506001600160a01b03831660009081526020818152604080832060050180548251818502810185019093528083529192909190830182828015610e9757602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610e79575b5050505050905060005b8151811015610f3057600080866001600160a01b03166001600160a01b031681526020019081526020016000206003016000838381518110610ef357634e487b7160e01b600052603260045260246000fd5b6020908102919091018101516001600160a01b03168252810191909152604001600020805460ff1916905580610f288161130e565b915050610ea1565b506001600160a01b0384166000908152602081905260408120805461ffff19168155600181018290556002810182905590610f6e6004830182611088565b610f7c600583016000611088565b505050505050565b6040805133815261ffff851660208201526001600160a01b03841681830152821515606082015290517f32b8c3a9535767c48b7f4575cbd5402f63b03ba60ee9767254ee701a41123b069181900360800190a16001600160a01b0382166000908152602081815260408083203384526003019091529020805460ff19166001179055801561104a576001600160a01b0382166000908152602081815260408220600401805460018101825590835291200180546001600160a01b03191633179055610726565b6001600160a01b0382166000908152602081815260408220600501805460018101825590835291200180546001600160a01b03191633179055505050565b50805460008255906000526020600020908101906110a691906110a9565b50565b5b808211156110be57600081556001016110aa565b5090565b80356001600160a01b03811681146107e957600080fd5b803561ffff811681146107e957600080fd5b6000602082840312156110fc578081fd5b6107c5826110c2565b60008060408385031215611117578081fd5b611120836110c2565b9150602083013561113081611367565b809150509250929050565b60008060006060848603121561114f578081fd5b611158846110c2565b9250602084013561116881611367565b929592945050506040919091013590565b60006020828403121561118a578081fd5b81516107c581611367565b6000806000606084860312156111a9578283fd5b6111b2846110d9565b92506111c0602085016110c2565b915060408401356111d081611367565b809150509250925092565b6000806000606084860312156111ef578283fd5b6111f8846110d9565b9250611206602085016110c2565b9150604084013590509250925092565b6000815480845260208085019450838352808320835b838110156112515781546001600160a01b03168752958201956001918201910161122c565b509495945050505050565b6020808252601a908201527f6d73672e73656e646572206973206e6f7420616e2061646d696e000000000000604082015260600190565b600067ffffffffffffffff85168252606060208301526112b66060830185611216565b82810360408401526112c88185611216565b9695505050505050565b600082198211156112e5576112e5611351565b500190565b600067ffffffffffffffff82168061130457611304611351565b6000190192915050565b600060001982141561132257611322611351565b5060010190565b600067ffffffffffffffff8083168181141561134757611347611351565b6001019392505050565b634e487b7160e01b600052601160045260246000fd5b80151581146110a657600080fdfea26469706673582212208d28f2b6e8af3fb434b7c5ad4250c5ceef4290668918040825e47510f543530764736f6c63430008020033";

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
