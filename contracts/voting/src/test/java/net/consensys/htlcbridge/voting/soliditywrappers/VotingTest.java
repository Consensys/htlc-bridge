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
 * <p>Generated with web3j version 4.8.7-SNAPSHOT.
 */
@SuppressWarnings("rawtypes")
public class VotingTest extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50610046336000908152600360205260409020805460ff19166001908117909155600280546001600160401b0319169091179055565b6114d8806100556000396000f3fe608060405234801561001057600080fd5b50600436106100cf5760003560e01c8063581cb2dc1161008c5780637bd7f87c116100665780637bd7f87c1461021e578063abc6814e14610231578063b2e4fece1461025f578063d69d6e6f1461028b57600080fd5b8063581cb2dc14610192578063633858f3146101ae578063721c3c64146101ee57600080fd5b8063079cf38a146100d4578063181d15ba146100fa57806323113b761461010f57806324d7806c1461013a5780633c6bb4361461017657806353f598b91461017f575b600080fd5b6100e76100e23660046111f5565b61029e565b6040519081526020015b60405180910390f35b61010d61010836600461123e565b6102ec565b005b61012261011d36600461127a565b610794565b6040516001600160a01b0390911681526020016100f1565b6101666101483660046112b9565b6001600160a01b031660009081526003602052604090205460ff1690565b60405190151581526020016100f1565b6100e760045481565b61010d61018d3660046112b9565b610819565b60025460405167ffffffffffffffff90911681526020016100f1565b6101db6101bc3660046112b9565b6001600160a01b031660009081526020819052604090205461ffff1690565b60405161ffff90911681526020016100f1565b600154604080516001600160a01b0383168152600160a01b90920467ffffffffffffffff166020830152016100f1565b61016661022c3660046112b9565b610a21565b61016661023f3660046112b9565b6001600160a01b0316600090815260208190526040902060020154421190565b6100e761026d3660046112b9565b6001600160a01b031660009081526020819052604090206002015490565b61010d6102993660046112d4565b610a42565b600081156102c857506001600160a01b0382166000908152602081905260409020600401546102e6565b506001600160a01b0382166000908152602081905260409020600501545b92915050565b3360009081526003602052604090205460ff166103245760405162461bcd60e51b815260040161031b9061131b565b60405180910390fd5b61032d82610a21565b1561037a5760405162461bcd60e51b815260206004820152601a60248201527f566f74696e6720616c726561647920696e2070726f6772657373000000000000604482015260640161031b565b61ffff83166103d65760405162461bcd60e51b815260206004820152602260248201527f566f74654e6f6e653a2043616e206e6f7420766f746520666f72206e6f7468696044820152616e6760f01b606482015260840161031b565b61ffff831660011415610463576001600160a01b03821660009081526003602052604090205460ff161561045e5760405162461bcd60e51b815260206004820152602960248201527f566f746541646441646d696e3a204164647265737320697320616c72656164796044820152681030b71030b236b4b760b91b606482015260840161031b565b610703565b61ffff83166002141561054e576001600160a01b03821660009081526003602052604090205460ff166104e95760405162461bcd60e51b815260206004820152602860248201527f566f746552656d6f766541646d696e3a2041646472657373206973206e6f742060448201526730b71030b236b4b760c11b606482015260840161031b565b6001600160a01b03821633141561045e5760405162461bcd60e51b8152602060048201526024808201527f566f746552656d6f766541646d696e3a2043616e206e6f742072656d6f76652060448201526339b2b63360e11b606482015260840161031b565b61ffff8316600314156106a3576040516301ffc9a760e01b81526319ab36c160e01b600482015282906001600160a01b038216906301ffc9a79060240160206040518083038186803b1580156105a357600080fd5b505afa1580156105b7573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906105db9190611352565b61064d5760405162461bcd60e51b815260206004820152603960248201527f566f74654368616e6765566f74696e673a2050726f706f73656420636f6e747260448201527f616374206e6f74206120766f74696e6720636f6e747261637400000000000000606482015260840161031b565b6000821161069d5760405162461bcd60e51b815260206004820152601b60248201527f50726f706f73656420766f74696e6720706572696f6420697320300000000000604482015260640161031b565b50610703565b606461ffff841610156106f85760405162461bcd60e51b815260206004820152601d60248201527f566f746541646d696e3a2074797065206e6f7420737570706f72746564000000604482015260640161031b565b610703838383610c26565b6001600160a01b0382166000908152602081905260409020805461ffff191661ffff851617905560015461074890600160a01b900467ffffffffffffffff1642611385565b6001600160a01b0380841660009081526020819052604090206002810192909255600191820183905590541661078857610783826001610cee565b505050565b6107838383600161105e565b600082156107e5576001600160a01b03841660009081526020819052604090206004018054839081106107c9576107c961139d565b6000918252602090912001546001600160a01b03169050610812565b6001600160a01b03841660009081526020819052604090206005018054839081106107c9576107c961139d565b9392505050565b3360009081526003602052604090205460ff166108485760405162461bcd60e51b815260040161031b9061131b565b61085181610a21565b61088f5760405162461bcd60e51b815260206004820152600f60248201526e566f7465206e6f742061637469766560881b604482015260640161031b565b6001600160a01b03811660009081526020819052604090206002015442116109035760405162461bcd60e51b815260206004820152602160248201527f566f74696e6720706572696f6420686173206e6f7420796574206578706972656044820152601960fa1b606482015260840161031b565b6001546002546001600160a01b0383811660009081526020819052604080822090516319ab36c160e01b81529290941693909284926319ab36c19261095d9267ffffffffffffffff169160048083019260050191016113fc565b60206040518083038186803b15801561097557600080fd5b505afa158015610989573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906109ad9190611352565b6001600160a01b0384166000818152602081815260409182902054825161ffff90911680825291810193909352831515838301529051929350917f9d4ce721e9765ae20e979179e0edb2b4d216041d2824a032c04687c7f0da764d9181900360600190a1610a1b8483610cee565b50505050565b6001600160a01b031660009081526020819052604090205461ffff16151590565b3360009081526003602052604090205460ff16610a715760405162461bcd60e51b815260040161031b9061131b565b610a7a82610a21565b610ab85760405162461bcd60e51b815260206004820152600f60248201526e566f7465206e6f742061637469766560881b604482015260640161031b565b6001600160a01b038216600090815260208190526040902060020154421115610b235760405162461bcd60e51b815260206004820152601960248201527f566f74696e6720706572696f6420686173206578706972656400000000000000604482015260640161031b565b6001600160a01b03821660009081526020818152604080832033845260030190915290205460ff1615610b985760405162461bcd60e51b815260206004820152601960248201527f4163636f756e742068617320616c726561647920766f74656400000000000000604482015260640161031b565b6001600160a01b03821660009081526020819052604090205461ffff848116911614610c1b5760405162461bcd60e51b815260206004820152602c60248201527f566f74696e6720616374696f6e20646f6573206e6f74206d617463682061637460448201526b1a5d99481c1c9bdc1bdcd85b60a21b606482015260840161031b565b61078383838361105e565b61ffff831660641415610ca6576001600160a01b038216156107835760405162461bcd60e51b815260206004820152603360248201527f417070566f74653a20766f746520746172676574206d757374206265207a65726044820152721bc81dda195b8818da185b99da5b99c81d985b606a1b606482015260840161031b565b60405162461bcd60e51b815260206004820181905260248201527f417070566f74653a20556e737570706f7274656420766f746520616374696f6e604482015260640161031b565b8015610e1d576001600160a01b0382166000908152602081905260409020805460019182015461ffff90911691821415610d89576001600160a01b0384166000908152600360205260408120805460ff191660011790556002805467ffffffffffffffff1691610d5d8361143b565b91906101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555050610e1a565b61ffff821660021415610dce576001600160a01b0384166000908152600360205260408120805460ff191690556002805467ffffffffffffffff1691610d5d83611463565b61ffff821660031415610e0f576001805467ffffffffffffffff8316600160a01b026001600160e01b03199091166001600160a01b03871617179055610e1a565b610e1a828583611162565b50505b6001600160a01b03821660009081526020818152604080832060040180548251818502810185019093528083529192909190830182828015610e8857602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610e6a575b5050505050905060005b8151811015610f1357600080856001600160a01b03166001600160a01b031681526020019081526020016000206003016000838381518110610ed657610ed661139d565b6020908102919091018101516001600160a01b03168252810191909152604001600020805460ff1916905580610f0b81611487565b915050610e92565b506001600160a01b03831660009081526020818152604080832060050180548251818502810185019093528083529192909190830182828015610f7f57602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610f61575b5050505050905060005b815181101561100a57600080866001600160a01b03166001600160a01b031681526020019081526020016000206003016000838381518110610fcd57610fcd61139d565b6020908102919091018101516001600160a01b03168252810191909152604001600020805460ff191690558061100281611487565b915050610f89565b506001600160a01b0384166000908152602081905260408120805461ffff191681556001810182905560028101829055906110486004830182611191565b611056600583016000611191565b505050505050565b6040805133815261ffff851660208201526001600160a01b03841681830152821515606082015290517f32b8c3a9535767c48b7f4575cbd5402f63b03ba60ee9767254ee701a41123b069181900360800190a16001600160a01b0382166000908152602081815260408083203384526003019091529020805460ff191660011790558015611124576001600160a01b0382166000908152602081815260408220600401805460018101825590835291200180546001600160a01b03191633179055505050565b6001600160a01b0382166000908152602081815260408220600501805460018101825590835291200180546001600160a01b03191633179055505050565b61ffff83166064141561078357506001600160a01b031660009081526020819052604090206001015460045550565b50805460008255906000526020600020908101906111af91906111b2565b50565b5b808211156111c757600081556001016111b3565b5090565b80356001600160a01b03811681146111e257600080fd5b919050565b80151581146111af57600080fd5b6000806040838503121561120857600080fd5b611211836111cb565b91506020830135611221816111e7565b809150509250929050565b803561ffff811681146111e257600080fd5b60008060006060848603121561125357600080fd5b61125c8461122c565b925061126a602085016111cb565b9150604084013590509250925092565b60008060006060848603121561128f57600080fd5b611298846111cb565b925060208401356112a8816111e7565b929592945050506040919091013590565b6000602082840312156112cb57600080fd5b610812826111cb565b6000806000606084860312156112e957600080fd5b6112f28461122c565b9250611300602085016111cb565b91506040840135611310816111e7565b809150509250925092565b6020808252601a908201527f6d73672e73656e646572206973206e6f7420616e2061646d696e000000000000604082015260600190565b60006020828403121561136457600080fd5b8151610812816111e7565b634e487b7160e01b600052601160045260246000fd5b600082198211156113985761139861136f565b500190565b634e487b7160e01b600052603260045260246000fd5b6000815480845260208085019450836000528060002060005b838110156113f15781546001600160a01b0316875295820195600191820191016113cc565b509495945050505050565b67ffffffffffffffff8416815260606020820152600061141f60608301856113b3565b828103604084015261143181856113b3565b9695505050505050565b600067ffffffffffffffff808316818114156114595761145961136f565b6001019392505050565b600067ffffffffffffffff82168061147d5761147d61136f565b6000190192915050565b600060001982141561149b5761149b61136f565b506001019056fea264697066735822122053c181e5450f475e27c46bdaa302ccd86c7fe1b422b335aed5435f848439aa4064736f6c63430008090033";

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
