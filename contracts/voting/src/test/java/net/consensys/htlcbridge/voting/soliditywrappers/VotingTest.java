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
    public static final String BINARY = "608060405234801561001057600080fd5b50610046336000908152600360205260409020805460ff19166001908117909155600280546001600160401b0319169091179055565b611155806100556000396000f3fe608060405234801561001057600080fd5b50600436106100935760003560e01c8063633858f311610066578063633858f314610104578063721c3c641461012a5780637bd7f87c1461015a578063abc6814e1461016d578063d69d6e6f1461019b57610093565b8063181d15ba1461009857806324d7806c146100ad57806353f598b9146100d5578063581cb2dc146100e8575b600080fd5b6100ab6100a6366004610f85565b6101ae565b005b6100c06100bb366004610f02565b6104a4565b60405190151581526020015b60405180910390f35b6100ab6100e3366004610f02565b6104c6565b60025460405167ffffffffffffffff90911681526020016100cc565b610117610112366004610f02565b6106f1565b60405161ffff90911681526020016100cc565b600154604080516001600160a01b0383168152600160a01b90920467ffffffffffffffff166020830152016100cc565b6100c0610168366004610f02565b610732565b6100c061017b366004610f02565b6001600160a01b0316600090815260208190526040902060020154421090565b6100ab6101a9366004610f3f565b610778565b3360009081526003602052604090205460ff166101e65760405162461bcd60e51b81526004016101dd90611006565b60405180910390fd5b6101ef82610732565b1561023c5760405162461bcd60e51b815260206004820152601a60248201527f566f74696e6720616c726561647920696e2070726f677265737300000000000060448201526064016101dd565b60008361ffff16600381111561026257634e487b7160e01b600052602160045260246000fd5b9050600181600381111561028657634e487b7160e01b600052602160045260246000fd5b14156102f957610295836104a4565b156102f45760405162461bcd60e51b815260206004820152602960248201527f566f746541646441646d696e3a204164647265737320697320616c72656164796044820152681030b71030b236b4b760b91b60648201526084016101dd565b6103ec565b600281600381111561031b57634e487b7160e01b600052602160045260246000fd5b14156103ec5761032a836104a4565b6103875760405162461bcd60e51b815260206004820152602860248201527f566f746552656d6f766541646d696e3a2041646472657373206973206e6f742060448201526730b71030b236b4b760c11b60648201526084016101dd565b6001600160a01b0383163314156103ec5760405162461bcd60e51b8152602060048201526024808201527f566f746552656d6f766541646d696e3a2043616e206e6f742072656d6f76652060448201526339b2b63360e11b60648201526084016101dd565b6001600160a01b0383166000908152602081905260409020805482919060ff1916600183600381111561042f57634e487b7160e01b600052602160045260246000fd5b021790555060015461045290600160a01b900467ffffffffffffffff164261107c565b6001600160a01b038085166000908152602081905260409020600281019290925560019182018490559054166104925761048d8360016109be565b61049e565b61049e84846001610d9a565b50505050565b6001600160a01b03811660009081526003602052604090205460ff165b919050565b3360009081526003602052604090205460ff166104f55760405162461bcd60e51b81526004016101dd90611006565b6104fe81610732565b61053c5760405162461bcd60e51b815260206004820152600f60248201526e566f7465206e6f742061637469766560881b60448201526064016101dd565b6001600160a01b03811660009081526020819052604090206002015442106105b05760405162461bcd60e51b815260206004820152602160248201527f566f74696e6720706572696f6420686173206e6f7420796574206578706972656044820152601960fa1b60648201526084016101dd565b6001546002546001600160a01b0383811660009081526020819052604080822090516319ab36c160e01b81529290941693909284926319ab36c19261060a9267ffffffffffffffff1691600480830192600501910161103d565b60206040518083038186803b15801561062257600080fd5b505afa158015610636573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061065a9190610f23565b6001600160a01b03841660009081526020819052604090205490915060ff167f9d4ce721e9765ae20e979179e0edb2b4d216041d2824a032c04687c7f0da764d8160038111156106ba57634e487b7160e01b600052602160045260246000fd5b6040805161ffff90921682526001600160a01b03871660208301528415159082015260600160405180910390a161049e84836109be565b6001600160a01b03811660009081526020819052604081205460ff16600381111561072c57634e487b7160e01b600052602160045260246000fd5b92915050565b6000806001600160a01b03831660009081526020819052604090205460ff16600381111561077057634e487b7160e01b600052602160045260246000fd5b141592915050565b3360009081526003602052604090205460ff166107a75760405162461bcd60e51b81526004016101dd90611006565b6107b082610732565b6107ee5760405162461bcd60e51b815260206004820152600f60248201526e566f7465206e6f742061637469766560881b60448201526064016101dd565b6001600160a01b0382166000908152602081905260409020600201544210156108595760405162461bcd60e51b815260206004820152601960248201527f566f74696e6720706572696f642068617320657870697265640000000000000060448201526064016101dd565b6001600160a01b03821660009081526020818152604080832033845260030190915290205460ff16156108ce5760405162461bcd60e51b815260206004820152601960248201527f4163636f756e742068617320616c726561647920766f7465640000000000000060448201526064016101dd565b60008361ffff1660038111156108f457634e487b7160e01b600052602160045260246000fd5b905080600381111561091657634e487b7160e01b600052602160045260246000fd5b6001600160a01b03841660009081526020819052604090205460ff16600381111561095157634e487b7160e01b600052602160045260246000fd5b146109b35760405162461bcd60e51b815260206004820152602c60248201527f566f74696e6720616374696f6e20646f6573206e6f74206d617463682061637460448201526b1a5d99481c1c9bdc1bdcd85b60a21b60648201526084016101dd565b61049e848484610d9a565b8015610b3e576001600160a01b0382166000908152602081905260409020805460019182015460ff90911691826003811115610a0a57634e487b7160e01b600052602160045260246000fd5b1415610a77576001600160a01b0384166000908152600360205260408120805460ff191660011790556002805467ffffffffffffffff1691610a4b836110d3565b91906101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555050610b3b565b6002826003811115610a9957634e487b7160e01b600052602160045260246000fd5b1415610ad7576001600160a01b0384166000908152600360205260408120805460ff191690556002805467ffffffffffffffff1691610a4b83611094565b6003826003811115610af957634e487b7160e01b600052602160045260246000fd5b1415610b3b57600180546001600160a01b0319166001600160a01b0386161767ffffffffffffffff60a01b1916600160a01b67ffffffffffffffff8416021790555b50505b6001600160a01b03821660009081526020818152604080832060040180548251818502810185019093528083529192909190830182828015610ba957602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610b8b575b5050505050905060005b8151811015610c4257600080856001600160a01b03166001600160a01b031681526020019081526020016000206003016000838381518110610c0557634e487b7160e01b600052603260045260246000fd5b6020908102919091018101516001600160a01b03168252810191909152604001600020805460ff1916905580610c3a816110b8565b915050610bb3565b506001600160a01b03831660009081526020818152604080832060050180548251818502810185019093528083529192909190830182828015610cae57602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610c90575b5050505050905060005b8151811015610d4757600080866001600160a01b03166001600160a01b031681526020019081526020016000206003016000838381518110610d0a57634e487b7160e01b600052603260045260246000fd5b6020908102919091018101516001600160a01b03168252810191909152604001600020805460ff1916905580610d3f816110b8565b915050610cb8565b506001600160a01b0384166000908152602081905260408120805460ff19168155600181018290556002810182905590610d846004830182610e9f565b610d92600583016000610e9f565b505050505050565b6040805133815261ffff851660208201526001600160a01b03841681830152821515606082015290517f32b8c3a9535767c48b7f4575cbd5402f63b03ba60ee9767254ee701a41123b069181900360800190a16001600160a01b0382166000908152602081815260408083203384526003019091529020805460ff191660011790558015610e60576001600160a01b0382166000908152602081815260408220600401805460018101825590835291200180546001600160a01b03191633179055610e9a565b6001600160a01b0382166000908152602081815260408220600501805460018101825590835291200180546001600160a01b031916331790555b505050565b5080546000825590600052602060002090810190610ebd9190610ec0565b50565b5b80821115610ed55760008155600101610ec1565b5090565b80356001600160a01b03811681146104c157600080fd5b803561ffff811681146104c157600080fd5b600060208284031215610f13578081fd5b610f1c82610ed9565b9392505050565b600060208284031215610f34578081fd5b8151610f1c81611111565b600080600060608486031215610f53578182fd5b610f5c84610ef0565b9250610f6a60208501610ed9565b91506040840135610f7a81611111565b809150509250925092565b600080600060608486031215610f99578283fd5b610fa284610ef0565b9250610fb060208501610ed9565b9150604084013590509250925092565b6000815480845260208085019450838352808320835b83811015610ffb5781546001600160a01b031687529582019560019182019101610fd6565b509495945050505050565b6020808252601a908201527f6d73672e73656e646572206973206e6f7420616e2061646d696e000000000000604082015260600190565b600067ffffffffffffffff85168252606060208301526110606060830185610fc0565b82810360408401526110728185610fc0565b9695505050505050565b6000821982111561108f5761108f6110fb565b500190565b600067ffffffffffffffff8216806110ae576110ae6110fb565b6000190192915050565b60006000198214156110cc576110cc6110fb565b5060010190565b600067ffffffffffffffff808316818114156110f1576110f16110fb565b6001019392505050565b634e487b7160e01b600052601160045260246000fd5b8015158114610ebd57600080fdfea2646970667358221220c7acfb0bc50c14a44bd6a1c3350454501e4a056226255403865eb87824ba5a4264736f6c63430008020033";

    public static final String FUNC_ACTIONVOTES = "actionVotes";

    public static final String FUNC_GETNUMADMINS = "getNumAdmins";

    public static final String FUNC_GETVOTINGCONFIG = "getVotingConfig";

    public static final String FUNC_ISADMIN = "isAdmin";

    public static final String FUNC_ISVOTEACTIVE = "isVoteActive";

    public static final String FUNC_PROPOSEVOTE = "proposeVote";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_VOTEPERIODEXPIRED = "votePeriodExpired";

    public static final String FUNC_VOTETYPE = "voteType";

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
