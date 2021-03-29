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
    public static final String BINARY = "608060405234801561001057600080fd5b50610046336000908152600360205260409020805460ff19166001908117909155600280546001600160401b0319169091179055565b6114e0806100556000396000f3fe608060405234801561001057600080fd5b50600436106100cf5760003560e01c8063581cb2dc1161008c5780637bd7f87c116100665780637bd7f87c14610205578063abc6814e14610218578063b2e4fece14610246578063d69d6e6f14610272576100cf565b8063581cb2dc14610179578063633858f314610195578063721c3c64146101d5576100cf565b8063079cf38a146100d4578063181d15ba146100fa57806323113b761461010f57806324d7806c1461013a5780633c6bb4361461015d57806353f598b914610166575b600080fd5b6100e76100e236600461123a565b610285565b6040519081526020015b60405180910390f35b61010d610108366004611310565b6102d3565b005b61012261011d366004611270565b61075a565b6040516001600160a01b0390911681526020016100f1565b61014d610148366004611220565b6107fb565b60405190151581526020016100f1565b6100e760045481565b61010d610174366004611220565b61081d565b60025460405167ffffffffffffffff90911681526020016100f1565b6101c26101a3366004611220565b6001600160a01b031660009081526020819052604090205461ffff1690565b60405161ffff90911681526020016100f1565b600154604080516001600160a01b0383168152600160a01b90920467ffffffffffffffff166020830152016100f1565b61014d610213366004611220565b610a25565b61014d610226366004611220565b6001600160a01b0316600090815260208190526040902060020154421190565b6100e7610254366004611220565b6001600160a01b031660009081526020819052604090206002015490565b61010d6102803660046112ca565b610a46565b600081156102af57506001600160a01b0382166000908152602081905260409020600401546102cd565b506001600160a01b0382166000908152602081905260409020600501545b92915050565b3360009081526003602052604090205460ff1661030b5760405162461bcd60e51b815260040161030290611391565b60405180910390fd5b61031482610a25565b156103615760405162461bcd60e51b815260206004820152601a60248201527f566f74696e6720616c726561647920696e2070726f67726573730000000000006044820152606401610302565b61ffff83166103bd5760405162461bcd60e51b815260206004820152602260248201527f566f74654e6f6e653a2043616e206e6f7420766f746520666f72206e6f7468696044820152616e6760f01b6064820152608401610302565b61ffff831660011415610437576103d3826107fb565b156104325760405162461bcd60e51b815260206004820152602960248201527f566f746541646441646d696e3a204164647265737320697320616c72656164796044820152681030b71030b236b4b760b91b6064820152608401610302565b6106c4565b61ffff83166002141561050f5761044d826107fb565b6104aa5760405162461bcd60e51b815260206004820152602860248201527f566f746552656d6f766541646d696e3a2041646472657373206973206e6f742060448201526730b71030b236b4b760c11b6064820152608401610302565b6001600160a01b0382163314156104325760405162461bcd60e51b8152602060048201526024808201527f566f746552656d6f766541646d696e3a2043616e206e6f742072656d6f76652060448201526339b2b63360e11b6064820152608401610302565b61ffff831660031415610664576040516301ffc9a760e01b81526319ab36c160e01b600482015282906001600160a01b038216906301ffc9a79060240160206040518083038186803b15801561056457600080fd5b505afa158015610578573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061059c91906112ae565b61060e5760405162461bcd60e51b815260206004820152603960248201527f566f74654368616e6765566f74696e673a2050726f706f73656420636f6e747260448201527f616374206e6f74206120766f74696e6720636f6e7472616374000000000000006064820152608401610302565b6000821161065e5760405162461bcd60e51b815260206004820152601b60248201527f50726f706f73656420766f74696e6720706572696f64206973203000000000006044820152606401610302565b506106c4565b606461ffff841610156106b95760405162461bcd60e51b815260206004820152601d60248201527f566f746541646d696e3a2074797065206e6f7420737570706f727465640000006044820152606401610302565b6106c4838383610c2a565b6001600160a01b0382166000908152602081905260409020805461ffff191661ffff851617905560015461070990600160a01b900467ffffffffffffffff1642611407565b6001600160a01b0380841660009081526020819052604090206002810192909255600191820183905590541661074957610744826001610cf2565b610755565b6107558383600161108a565b505050565b600082156107b9576001600160a01b038416600090815260208190526040902060040180548390811061079d57634e487b7160e01b600052603260045260246000fd5b6000918252602090912001546001600160a01b031690506107f4565b6001600160a01b038416600090815260208190526040902060050180548390811061079d57634e487b7160e01b600052603260045260246000fd5b9392505050565b6001600160a01b03811660009081526003602052604090205460ff165b919050565b3360009081526003602052604090205460ff1661084c5760405162461bcd60e51b815260040161030290611391565b61085581610a25565b6108935760405162461bcd60e51b815260206004820152600f60248201526e566f7465206e6f742061637469766560881b6044820152606401610302565b6001600160a01b03811660009081526020819052604090206002015442116109075760405162461bcd60e51b815260206004820152602160248201527f566f74696e6720706572696f6420686173206e6f7420796574206578706972656044820152601960fa1b6064820152608401610302565b6001546002546001600160a01b0383811660009081526020819052604080822090516319ab36c160e01b81529290941693909284926319ab36c1926109619267ffffffffffffffff169160048083019260050191016113c8565b60206040518083038186803b15801561097957600080fd5b505afa15801561098d573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906109b191906112ae565b6001600160a01b0384166000818152602081815260409182902054825161ffff90911680825291810193909352831515838301529051929350917f9d4ce721e9765ae20e979179e0edb2b4d216041d2824a032c04687c7f0da764d9181900360600190a1610a1f8483610cf2565b50505050565b6001600160a01b031660009081526020819052604090205461ffff16151590565b3360009081526003602052604090205460ff16610a755760405162461bcd60e51b815260040161030290611391565b610a7e82610a25565b610abc5760405162461bcd60e51b815260206004820152600f60248201526e566f7465206e6f742061637469766560881b6044820152606401610302565b6001600160a01b038216600090815260208190526040902060020154421115610b275760405162461bcd60e51b815260206004820152601960248201527f566f74696e6720706572696f64206861732065787069726564000000000000006044820152606401610302565b6001600160a01b03821660009081526020818152604080832033845260030190915290205460ff1615610b9c5760405162461bcd60e51b815260206004820152601960248201527f4163636f756e742068617320616c726561647920766f746564000000000000006044820152606401610302565b6001600160a01b03821660009081526020819052604090205461ffff848116911614610c1f5760405162461bcd60e51b815260206004820152602c60248201527f566f74696e6720616374696f6e20646f6573206e6f74206d617463682061637460448201526b1a5d99481c1c9bdc1bdcd85b60a21b6064820152608401610302565b61075583838361108a565b61ffff831660641415610caa576001600160a01b038216156107445760405162461bcd60e51b815260206004820152603360248201527f417070566f74653a20766f746520746172676574206d757374206265207a65726044820152721bc81dda195b8818da185b99da5b99c81d985b606a1b6064820152608401610302565b60405162461bcd60e51b815260206004820181905260248201527f417070566f74653a20556e737570706f7274656420766f746520616374696f6e6044820152606401610302565b8015610e2d576001600160a01b0382166000908152602081905260409020805460019182015461ffff90911691821415610d8d576001600160a01b0384166000908152600360205260408120805460ff191660011790556002805467ffffffffffffffff1691610d618361145e565b91906101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555050610e2a565b61ffff821660021415610dd2576001600160a01b0384166000908152600360205260408120805460ff191690556002805467ffffffffffffffff1691610d618361141f565b61ffff821660031415610e1f57600180546001600160a01b0319166001600160a01b0386161767ffffffffffffffff60a01b1916600160a01b67ffffffffffffffff841602179055610e2a565b610e2a82858361118e565b50505b6001600160a01b03821660009081526020818152604080832060040180548251818502810185019093528083529192909190830182828015610e9857602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610e7a575b5050505050905060005b8151811015610f3157600080856001600160a01b03166001600160a01b031681526020019081526020016000206003016000838381518110610ef457634e487b7160e01b600052603260045260246000fd5b6020908102919091018101516001600160a01b03168252810191909152604001600020805460ff1916905580610f2981611443565b915050610ea2565b506001600160a01b03831660009081526020818152604080832060050180548251818502810185019093528083529192909190830182828015610f9d57602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610f7f575b5050505050905060005b815181101561103657600080866001600160a01b03166001600160a01b031681526020019081526020016000206003016000838381518110610ff957634e487b7160e01b600052603260045260246000fd5b6020908102919091018101516001600160a01b03168252810191909152604001600020805460ff191690558061102e81611443565b915050610fa7565b506001600160a01b0384166000908152602081905260408120805461ffff1916815560018101829055600281018290559061107460048301826111bd565b6110826005830160006111bd565b505050505050565b6040805133815261ffff851660208201526001600160a01b03841681830152821515606082015290517f32b8c3a9535767c48b7f4575cbd5402f63b03ba60ee9767254ee701a41123b069181900360800190a16001600160a01b0382166000908152602081815260408083203384526003019091529020805460ff191660011790558015611150576001600160a01b0382166000908152602081815260408220600401805460018101825590835291200180546001600160a01b03191633179055610755565b6001600160a01b0382166000908152602081815260408220600501805460018101825590835291200180546001600160a01b03191633179055505050565b61ffff83166064141561075557506001600160a01b031660009081526020819052604090206001015460045550565b50805460008255906000526020600020908101906111db91906111de565b50565b5b808211156111f357600081556001016111df565b5090565b80356001600160a01b038116811461081857600080fd5b803561ffff8116811461081857600080fd5b600060208284031215611231578081fd5b6107f4826111f7565b6000806040838503121561124c578081fd5b611255836111f7565b915060208301356112658161149c565b809150509250929050565b600080600060608486031215611284578081fd5b61128d846111f7565b9250602084013561129d8161149c565b929592945050506040919091013590565b6000602082840312156112bf578081fd5b81516107f48161149c565b6000806000606084860312156112de578283fd5b6112e78461120e565b92506112f5602085016111f7565b915060408401356113058161149c565b809150509250925092565b600080600060608486031215611324578283fd5b61132d8461120e565b925061133b602085016111f7565b9150604084013590509250925092565b6000815480845260208085019450838352808320835b838110156113865781546001600160a01b031687529582019560019182019101611361565b509495945050505050565b6020808252601a908201527f6d73672e73656e646572206973206e6f7420616e2061646d696e000000000000604082015260600190565b600067ffffffffffffffff85168252606060208301526113eb606083018561134b565b82810360408401526113fd818561134b565b9695505050505050565b6000821982111561141a5761141a611486565b500190565b600067ffffffffffffffff82168061143957611439611486565b6000190192915050565b600060001982141561145757611457611486565b5060010190565b600067ffffffffffffffff8083168181141561147c5761147c611486565b6001019392505050565b634e487b7160e01b600052601160045260246000fd5b80151581146111db57600080fdfea26469706673582212204536e0048e9bb71b565cc9cf6d2a8884b604ac0c7260b8187fc3bda6f4d4ac8664736f6c63430008020033";

    public static final String FUNC_ACTIONVOTES = "actionVotes";

    public static final String FUNC_ENDOFVOTINGPERIOD = "endOfVotingPeriod";

    public static final String FUNC_GETNUMADMINS = "getNumAdmins";

    public static final String FUNC_GETVOTINGCONFIG = "getVotingConfig";

    public static final String FUNC_ISADMIN = "isAdmin";

    public static final String FUNC_ISVOTEACTIVE = "isVoteActive";

    public static final String FUNC_NUMVOTES = "numVotes";

    public static final String FUNC_PROPOSEVOTE = "proposeVote";

    public static final String FUNC_VAL = "val";

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

    public RemoteFunctionCall<BigInteger> val() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_VAL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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
