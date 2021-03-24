package net.consensys.htlcbridge.transfer.soliditywrappers;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple7;
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
public class Erc20HtlcReceiver extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50604051610d7a380380610d7a83398101604081905261002f91610050565b6000908155338152600260205260409020805460ff19166001179055610068565b600060208284031215610061578081fd5b5051919050565b610d03806100776000396000f3fe608060405234801561001057600080fd5b50600436106100f55760003560e01c80636742b7fd116100975780637a02dc06116100665780637a02dc061461022057806390469a9d14610279578063cbe230c31461028c578063e744092e146102ba576100f5565b80636742b7fd146101cc5780636747b1e9146101f157806372253f111461020457806378446bc114610217576100f5565b8063397bc641116100d3578063397bc6411461015a5780633d9534b71461017b5780634e95b54e146101a65780635f87fba9146101b9576100f5565b80630bb44109146100fa5780632d34b0ab1461010f578063305e94cd14610147575b600080fd5b61010d610108366004610b85565b6102fb565b005b61013261011d366004610b85565b60026020526000908152604090205460ff1681565b60405190151581526020015b60405180910390f35b61010d610155366004610ba6565b610357565b61016d610168366004610c39565b6103f1565b60405190815260200161013e565b610132610189366004610c39565b6000908152600360205260409020546001600160a01b0316151590565b6101326101b4366004610c51565b61042b565b61010d6101c7366004610bd8565b610460565b6101326101da366004610c39565b600090815260036020526040902060060154421190565b61010d6101ff366004610c51565b6106ff565b61010d610212366004610b85565b6109cc565b61016d60005481565b61023361022e366004610c39565b610a1c565b604080516001600160a01b039889168152968816602088015294909616938501939093526060840191909152608083015260a082015260c081019190915260e00161013e565b61010d610287366004610b85565b610ae2565b61013261029a366004610b85565b6001600160a01b0390811660009081526001602052604090205416151590565b6102e36102c8366004610b85565b6001602052600090815260409020546001600160a01b031681565b6040516001600160a01b03909116815260200161013e565b3360009081526002602052604090205460ff166103335760405162461bcd60e51b815260040161032a90610c72565b60405180910390fd5b6001600160a01b03166000908152600260205260409020805460ff19166001179055565b3360009081526002602052604090205460ff166103865760405162461bcd60e51b815260040161032a90610c72565b6001600160a01b0382811660008181526001602090815260409182902080546001600160a01b031916948616948517905581519283528201929092527f514ea6e2b7075d86113d432ebaab7f005e1e03d59f106b2d45cc37fae6211749910160405180910390a15050565b60008181526003602052604081206006015442111561041257506003610426565b506000818152600360205260409020600701545b919050565b60008160405160200161044091815260200190565b604051602081830303815290604052805190602001208314905092915050565b3360009081526002602052604090205460ff1661048f5760405162461bcd60e51b815260040161032a90610c72565b6000818152600360205260409020546001600160a01b0316156104f45760405162461bcd60e51b815260206004820152601760248201527f5472616e7366657220616c726561647920657869737473000000000000000000604482015260640161032a565b6001600160a01b03808516600090815260016020526040902054166105545760405162461bcd60e51b8152602060048201526016602482015275546f6b656e206e6f74207472616e7366657261626c6560501b604482015260640161032a565b600080546105629042610ca9565b9050604051806101000160405280336001600160a01b03168152602001856001600160a01b03168152602001866001600160a01b031681526020018481526020018381526020016000801b815260200182815260200160008152506003600084815260200190815260200160002060008201518160000160006101000a8154816001600160a01b0302191690836001600160a01b0316021790555060208201518160010160006101000a8154816001600160a01b0302191690836001600160a01b0316021790555060408201518160020160006101000a8154816001600160a01b0302191690836001600160a01b03160217905550606082015181600301556080820151816004015560a0820151816005015560c0820151816006015560e08201518160070155905050836001600160a01b0316336001600160a01b0316837fe2860a3c68b9d06df3a13686e1972348a700ebb568ee65d3d9a9529aaf640dfc8887866040516106f0939291906001600160a01b039390931683526020830191909152604082015260600190565b60405180910390a45050505050565b6000828152600360205260409020546001600160a01b031661075d5760405162461bcd60e51b8152602060048201526017602482015276151c985b9cd9995c88191bd95cc81b9bdd08195e1a5cdd604a1b604482015260640161032a565b610767828261042b565b6107be5760405162461bcd60e51b815260206004820152602260248201527f507265696d61676520646f6573206e6f74206d6174636820636f6d6d69746d656044820152611b9d60f21b606482015260840161032a565b6000828152600360205260409020600701541561081d5760405162461bcd60e51b815260206004820152601a60248201527f5472616e73666572206e6f7420696e206f70656e207374617465000000000000604482015260640161032a565b6000828152600360205260409020600601544211156108735760405162461bcd60e51b8152602060048201526012602482015271151c985b9cd9995c881d1a5b59590b5bdd5d60721b604482015260640161032a565b600082815260036020818152604080842060028101546001600160a01b03908116808752600180865284882054978a905294869052938201549190940154915163a9059cbb60e01b815290841660048201526024810191909152909290911690819063a9059cbb90604401602060405180830381600087803b1580156108f857600080fd5b505af115801561090c573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906109309190610c19565b61096d5760405162461bcd60e51b815260206004820152600e60248201526d1d1c985b99995c8819985a5b195960921b604482015260640161032a565b600084815260036020526040908190206005810185905560026007909101555184907fd24fc1398ce9e9ac8a8ffe03705a3466fa8931ec89fb069768a14dfa139704fd906109be9086815260200190565b60405180910390a250505050565b3360009081526002602052604090205460ff166109fb5760405162461bcd60e51b815260040161032a90610c72565b6001600160a01b03166000908152600260205260409020805460ff19169055565b6000806000806000806000610a48886000908152600360205260409020546001600160a01b0316151590565b610a8e5760405162461bcd60e51b8152602060048201526017602482015276151c985b9cd9995c88191bd95cc81b9bdd08195e1a5cdd604a1b604482015260640161032a565b5050506000948552505060036020819052604090932080546001820154600283015495830154600584015460068501546007909501546001600160a01b039485169993851698949094169650909450929190565b3360009081526002602052604090205460ff16610b115760405162461bcd60e51b815260040161032a90610c72565b6001600160a01b03811660008181526001602090815260409182902080546001600160a01b031916905590519182527fbf996b4fd74f0c7159bb017b1db415b0d9a6f13129f46d0b93309d170b78df31910160405180910390a150565b80356001600160a01b038116811461042657600080fd5b600060208284031215610b96578081fd5b610b9f82610b6e565b9392505050565b60008060408385031215610bb8578081fd5b610bc183610b6e565b9150610bcf60208401610b6e565b90509250929050565b60008060008060808587031215610bed578182fd5b610bf685610b6e565b9350610c0460208601610b6e565b93969395505050506040820135916060013590565b600060208284031215610c2a578081fd5b81518015158114610b9f578182fd5b600060208284031215610c4a578081fd5b5035919050565b60008060408385031215610c63578182fd5b50508035926020909101359150565b60208082526019908201527f4e6f7420616e20617574686f72697365642072656c6179657200000000000000604082015260600190565b60008219821115610cc857634e487b7160e01b81526011600452602481fd5b50019056fea2646970667358221220fb1717d08b0a12037c1d630e1be88a7b9c5d106bdaf1b89211ce036a6768bccb64736f6c63430008020033";

    public static final String FUNC_ADDALLOWEDTOKEN = "addAllowedToken";

    public static final String FUNC_ADDAUTHORISEDRELAYER = "addAuthorisedRelayer";

    public static final String FUNC_ALLOWEDTOKENS = "allowedTokens";

    public static final String FUNC_AUTHORISEDRELAYER = "authorisedRelayer";

    public static final String FUNC_FINALISETRANSFERFROMOTHERBLOCKCHAIN = "finaliseTransferFromOtherBlockchain";

    public static final String FUNC_GETINFO = "getInfo";

    public static final String FUNC_ISALLOWEDTOKEN = "isAllowedToken";

    public static final String FUNC_NEWTRANSFERFROMOTHERBLOCKCHAIN = "newTransferFromOtherBlockchain";

    public static final String FUNC_PREIMAGEMATCHESCOMMITMENT = "preimageMatchesCommitment";

    public static final String FUNC_REMOVEALLOWEDTOKEN = "removeAllowedToken";

    public static final String FUNC_REMOVEAUTHORISEDRELAYER = "removeAuthorisedRelayer";

    public static final String FUNC_TIMELOCKPERIOD = "timeLockPeriod";

    public static final String FUNC_TRANSFEREXISTS = "transferExists";

    public static final String FUNC_TRANSFEREXPIRED = "transferExpired";

    public static final String FUNC_TRANSFERSTATE = "transferState";

    public static final Event ALLOWEDTOKENADDED_EVENT = new Event("AllowedTokenAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event ALLOWEDTOKENREMOVED_EVENT = new Event("AllowedTokenRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event TRANSFERCOMPLETED_EVENT = new Event("TransferCompleted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>() {}));
    ;

    public static final Event TRANSFERINIT_EVENT = new Event("TransferInit", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSFERREFUNDED_EVENT = new Event("TransferRefunded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}));
    ;

    @Deprecated
    protected Erc20HtlcReceiver(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Erc20HtlcReceiver(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Erc20HtlcReceiver(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Erc20HtlcReceiver(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<AllowedTokenAddedEventResponse> getAllowedTokenAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ALLOWEDTOKENADDED_EVENT, transactionReceipt);
        ArrayList<AllowedTokenAddedEventResponse> responses = new ArrayList<AllowedTokenAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AllowedTokenAddedEventResponse typedResponse = new AllowedTokenAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.otherBlockchainTokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.thisBlockchainTokenContract = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AllowedTokenAddedEventResponse> allowedTokenAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AllowedTokenAddedEventResponse>() {
            @Override
            public AllowedTokenAddedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ALLOWEDTOKENADDED_EVENT, log);
                AllowedTokenAddedEventResponse typedResponse = new AllowedTokenAddedEventResponse();
                typedResponse.log = log;
                typedResponse.otherBlockchainTokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.thisBlockchainTokenContract = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AllowedTokenAddedEventResponse> allowedTokenAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ALLOWEDTOKENADDED_EVENT));
        return allowedTokenAddedEventFlowable(filter);
    }

    public List<AllowedTokenRemovedEventResponse> getAllowedTokenRemovedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ALLOWEDTOKENREMOVED_EVENT, transactionReceipt);
        ArrayList<AllowedTokenRemovedEventResponse> responses = new ArrayList<AllowedTokenRemovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AllowedTokenRemovedEventResponse typedResponse = new AllowedTokenRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AllowedTokenRemovedEventResponse> allowedTokenRemovedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AllowedTokenRemovedEventResponse>() {
            @Override
            public AllowedTokenRemovedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ALLOWEDTOKENREMOVED_EVENT, log);
                AllowedTokenRemovedEventResponse typedResponse = new AllowedTokenRemovedEventResponse();
                typedResponse.log = log;
                typedResponse.tokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AllowedTokenRemovedEventResponse> allowedTokenRemovedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ALLOWEDTOKENREMOVED_EVENT));
        return allowedTokenRemovedEventFlowable(filter);
    }

    public List<TransferCompletedEventResponse> getTransferCompletedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFERCOMPLETED_EVENT, transactionReceipt);
        ArrayList<TransferCompletedEventResponse> responses = new ArrayList<TransferCompletedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferCompletedEventResponse typedResponse = new TransferCompletedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.commitment = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.preimage = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferCompletedEventResponse> transferCompletedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TransferCompletedEventResponse>() {
            @Override
            public TransferCompletedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFERCOMPLETED_EVENT, log);
                TransferCompletedEventResponse typedResponse = new TransferCompletedEventResponse();
                typedResponse.log = log;
                typedResponse.commitment = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.preimage = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferCompletedEventResponse> transferCompletedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFERCOMPLETED_EVENT));
        return transferCompletedEventFlowable(filter);
    }

    public List<TransferInitEventResponse> getTransferInitEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFERINIT_EVENT, transactionReceipt);
        ArrayList<TransferInitEventResponse> responses = new ArrayList<TransferInitEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferInitEventResponse typedResponse = new TransferInitEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.commitment = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.relayer = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.recipient = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.tokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.timeLock = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferInitEventResponse> transferInitEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TransferInitEventResponse>() {
            @Override
            public TransferInitEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFERINIT_EVENT, log);
                TransferInitEventResponse typedResponse = new TransferInitEventResponse();
                typedResponse.log = log;
                typedResponse.commitment = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.relayer = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.recipient = (String) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.tokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.timeLock = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferInitEventResponse> transferInitEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFERINIT_EVENT));
        return transferInitEventFlowable(filter);
    }

    public List<TransferRefundedEventResponse> getTransferRefundedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFERREFUNDED_EVENT, transactionReceipt);
        ArrayList<TransferRefundedEventResponse> responses = new ArrayList<TransferRefundedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferRefundedEventResponse typedResponse = new TransferRefundedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.commitment = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferRefundedEventResponse> transferRefundedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TransferRefundedEventResponse>() {
            @Override
            public TransferRefundedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFERREFUNDED_EVENT, log);
                TransferRefundedEventResponse typedResponse = new TransferRefundedEventResponse();
                typedResponse.log = log;
                typedResponse.commitment = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferRefundedEventResponse> transferRefundedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFERREFUNDED_EVENT));
        return transferRefundedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addAllowedToken(String _otherBlockchainTokenContract, String _thisBlockchainTokenContract) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDALLOWEDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _otherBlockchainTokenContract), 
                new org.web3j.abi.datatypes.Address(160, _thisBlockchainTokenContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addAuthorisedRelayer(String _newRelayer) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDAUTHORISEDRELAYER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _newRelayer)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> allowedTokens(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ALLOWEDTOKENS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> authorisedRelayer(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_AUTHORISEDRELAYER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> finaliseTransferFromOtherBlockchain(byte[] _commitment, byte[] _preimage) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_FINALISETRANSFERFROMOTHERBLOCKCHAIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment), 
                new org.web3j.abi.datatypes.generated.Bytes32(_preimage)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple7<String, String, String, BigInteger, byte[], BigInteger, BigInteger>> getInfo(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETINFO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple7<String, String, String, BigInteger, byte[], BigInteger, BigInteger>>(function,
                new Callable<Tuple7<String, String, String, BigInteger, byte[], BigInteger, BigInteger>>() {
                    @Override
                    public Tuple7<String, String, String, BigInteger, byte[], BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<String, String, String, BigInteger, byte[], BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (byte[]) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Boolean> isAllowedToken(String _tokenContract) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISALLOWEDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenContract)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> newTransferFromOtherBlockchain(String _otherBlockchainTokenContract, String _recipient, BigInteger _amount, byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_NEWTRANSFERFROMOTHERBLOCKCHAIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _otherBlockchainTokenContract), 
                new org.web3j.abi.datatypes.Address(160, _recipient), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount), 
                new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> preimageMatchesCommitment(byte[] _commitment, byte[] _preimage) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PREIMAGEMATCHESCOMMITMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment), 
                new org.web3j.abi.datatypes.generated.Bytes32(_preimage)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> removeAllowedToken(String _otherBlockchainTokenContract) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEALLOWEDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _otherBlockchainTokenContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeAuthorisedRelayer(String _relayerToBeRemoved) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEAUTHORISEDRELAYER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _relayerToBeRemoved)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> timeLockPeriod() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TIMELOCKPERIOD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> transferExists(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TRANSFEREXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> transferExpired(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TRANSFEREXPIRED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> transferState(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TRANSFERSTATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static Erc20HtlcReceiver load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Erc20HtlcReceiver(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Erc20HtlcReceiver load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Erc20HtlcReceiver(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Erc20HtlcReceiver load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Erc20HtlcReceiver(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Erc20HtlcReceiver load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Erc20HtlcReceiver(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Erc20HtlcReceiver> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger _timeLock) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_timeLock)));
        return deployRemoteCall(Erc20HtlcReceiver.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Erc20HtlcReceiver> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _timeLock) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_timeLock)));
        return deployRemoteCall(Erc20HtlcReceiver.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Erc20HtlcReceiver> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _timeLock) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_timeLock)));
        return deployRemoteCall(Erc20HtlcReceiver.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Erc20HtlcReceiver> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _timeLock) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_timeLock)));
        return deployRemoteCall(Erc20HtlcReceiver.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class AllowedTokenAddedEventResponse extends BaseEventResponse {
        public String otherBlockchainTokenContract;

        public String thisBlockchainTokenContract;
    }

    public static class AllowedTokenRemovedEventResponse extends BaseEventResponse {
        public String tokenContract;
    }

    public static class TransferCompletedEventResponse extends BaseEventResponse {
        public byte[] commitment;

        public byte[] preimage;
    }

    public static class TransferInitEventResponse extends BaseEventResponse {
        public byte[] commitment;

        public String relayer;

        public String recipient;

        public String tokenContract;

        public BigInteger amount;

        public BigInteger timeLock;
    }

    public static class TransferRefundedEventResponse extends BaseEventResponse {
        public byte[] commitment;
    }
}
