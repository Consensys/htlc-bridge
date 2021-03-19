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
import org.web3j.tuples.generated.Tuple6;
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
public class Erc20HtlcTransfer extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50604051610a3b380380610a3b83398101604081905261002f91610037565b60005561004f565b600060208284031215610048578081fd5b5051919050565b6109dd8061005e6000396000f3fe608060405234801561001057600080fd5b50600436106100b45760003560e01c80634e95b54e116100715780634e95b54e1461016857806378446bc11461017b5780637a02dc061461018457806390469a9d146101cf578063cbe230c3146101e2578063e744092e1461020e576100b4565b80630c8d3698146100b957806317b56fd3146100ce578063397bc641146100e15780633d9534b7146101075780634178617f146101425780634ca96f4b14610155575b600080fd5b6100cc6100c73660046108f2565b610231565b005b6100cc6100dc3660046108c2565b610334565b6100f46100ef3660046108c2565b6103c4565b6040519081526020015b60405180910390f35b6101326101153660046108c2565b6000908152600260205260409020546001600160a01b0316151590565b60405190151581526020016100fe565b6100cc61015036600461084f565b6103dc565b6100cc610163366004610870565b610438565b6101326101763660046108f2565b6106de565b6100f460005481565b6101976101923660046108c2565b610758565b604080516001600160a01b039788168152969095166020870152938501929092526060840152608083015260a082015260c0016100fe565b6100cc6101dd36600461084f565b6107e7565b6101326101f036600461084f565b6001600160a01b031660009081526001602052604090205460ff1690565b61013261021c36600461084f565b60016020526000908152604090205460ff1681565b6000828152600260205260409020546001600160a01b031661026e5760405162461bcd60e51b81526004016102659061094c565b60405180910390fd5b61027882826106de565b6102cf5760405162461bcd60e51b815260206004820152602260248201527f507265696d61676520646f6573206e6f74206d6174636820636f6d6d69746d656044820152611b9d60f21b6064820152608401610265565b600082815260026020526040902060060154156102eb57600080fd5b600082815260026020819052604080832060048101859055600601919091555183917fba4bce30c5ed2285a6e0a9a31a093123e7ab3a64b71d9e641d33c08e91f58b9a91a25050565b6000818152600260205260409020546001600160a01b03166103685760405162461bcd60e51b81526004016102659061094c565b6000818152600260205260409020600601541561038457600080fd5b60008181526002602052604080822060016006909101555182917f82239098982a89db70c5c60fcaa6f0d47aeb1d1e248893d0a4a3fe52f11fead191a250565b6000818152600260205260409020600601545b919050565b6001600160a01b038116600081815260016020818152604092839020805460ff191690921790915590519182527ff849d00bc67e9e4bf072df9680646c59b8c5380160834c2e1d4896ce5ec81f7591015b60405180910390a150565b6000818152600260205260409020546001600160a01b03161561049d5760405162461bcd60e51b815260206004820152601760248201527f5472616e7366657220616c7265616479206578697374730000000000000000006044820152606401610265565b6001600160a01b03831660009081526001602052604090205460ff166105055760405162461bcd60e51b815260206004820152601760248201527f546f6b656e206e6f74207472616e736665727261626c650000000000000000006044820152606401610265565b6040516323b872dd60e01b8152336004820152306024820152604481018390526001600160a01b038416906323b872dd90606401602060405180830381600087803b15801561055357600080fd5b505af1158015610567573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061058b91906108a2565b6105c95760405162461bcd60e51b815260206004820152600f60248201526e1d1c985b9cd9995c8819985a5b1959608a1b6044820152606401610265565b600080546105d79042610983565b6040805160e081018252338082526001600160a01b0388811660208085019182528486018a8152606086018a815260006080880181815260a089018b815260c08a018381528e8452600296879052928b902099518a549089166001600160a01b0319918216178b55965160018b0180549190991697169690961790965591519287019290925590516003860155915160048501555160058401555160069092019190915590519192509083907f1dfe9f774377e5f342d8d5ad811c54088db38526f3b0e43fbd58cd79e7ca53b8906106d0908890889087906001600160a01b039390931683526020830191909152604082015260600190565b60405180910390a350505050565b60006002826040516020016106f591815260200190565b60408051601f198184030181529082905261070f91610913565b602060405180830381855afa15801561072c573d6000803e3d6000fd5b5050506040513d601f19601f8201168201806040525081019061074f91906108da565b90921492915050565b600080600080600080610782876000908152600260205260409020546001600160a01b0316151590565b61079e5760405162461bcd60e51b81526004016102659061094c565b505050600093845250506002602081905260409092208054600182015493820154600483015460058401546006909401546001600160a01b039384169793909616959194509291565b6001600160a01b038116600081815260016020908152604091829020805460ff1916905590519182527fbf996b4fd74f0c7159bb017b1db415b0d9a6f13129f46d0b93309d170b78df31910161042d565b80356001600160a01b03811681146103d757600080fd5b600060208284031215610860578081fd5b61086982610838565b9392505050565b600080600060608486031215610884578182fd5b61088d84610838565b95602085013595506040909401359392505050565b6000602082840312156108b3578081fd5b81518015158114610869578182fd5b6000602082840312156108d3578081fd5b5035919050565b6000602082840312156108eb578081fd5b5051919050565b60008060408385031215610904578182fd5b50508035926020909101359150565b60008251815b818110156109335760208186018101518583015201610919565b818111156109415782828501525b509190910192915050565b60208082526017908201527f5472616e7366657220646f6573206e6f74206578697374000000000000000000604082015260600190565b600082198211156109a257634e487b7160e01b81526011600452602481fd5b50019056fea2646970667358221220f4cac31c0de6cf0178000fb6973e8527b66f50024b3a76ce9d30e8d76668460564736f6c63430008020033";

    public static final String FUNC_ADDALLOWEDTOKEN = "addAllowedToken";

    public static final String FUNC_ALLOWEDTOKENS = "allowedTokens";

    public static final String FUNC_FINALISETRANSFERTOOTHERBLOCKCHAIN = "finaliseTransferToOtherBlockchain";

    public static final String FUNC_GETINFO = "getInfo";

    public static final String FUNC_ISALLOWEDTOKEN = "isAllowedToken";

    public static final String FUNC_NEWTRANSFERTOOTHERBLOCKCHAIN = "newTransferToOtherBlockchain";

    public static final String FUNC_PREIMAGEMATCHESCOMMITMENT = "preimageMatchesCommitment";

    public static final String FUNC_REFUNDTRANSFERTOOTHERBLOCKCHAIN = "refundTransferToOtherBlockchain";

    public static final String FUNC_REMOVEALLOWEDTOKEN = "removeAllowedToken";

    public static final String FUNC_TIMELOCKPERIOD = "timeLockPeriod";

    public static final String FUNC_TRANSFEREXISTS = "transferExists";

    public static final String FUNC_TRANSFERSTATE = "transferState";

    public static final Event ALLOWEDTOKENADDED_EVENT = new Event("AllowedTokenAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event ALLOWEDTOKENREMOVED_EVENT = new Event("AllowedTokenRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event TRANSFERCOMPLETED_EVENT = new Event("TransferCompleted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}));
    ;

    public static final Event TRANSFERINIT_EVENT = new Event("TransferInit", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSFERREFUNDED_EVENT = new Event("TransferRefunded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}));
    ;

    @Deprecated
    protected Erc20HtlcTransfer(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Erc20HtlcTransfer(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Erc20HtlcTransfer(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Erc20HtlcTransfer(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<AllowedTokenAddedEventResponse> getAllowedTokenAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ALLOWEDTOKENADDED_EVENT, transactionReceipt);
        ArrayList<AllowedTokenAddedEventResponse> responses = new ArrayList<AllowedTokenAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AllowedTokenAddedEventResponse typedResponse = new AllowedTokenAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
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
                typedResponse.tokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
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
            typedResponse.sender = (String) eventValues.getIndexedValues().get(1).getValue();
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
                typedResponse.sender = (String) eventValues.getIndexedValues().get(1).getValue();
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

    public RemoteFunctionCall<TransactionReceipt> addAllowedToken(String _tokenContract) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDALLOWEDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> allowedTokens(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ALLOWEDTOKENS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> finaliseTransferToOtherBlockchain(byte[] _commitment, byte[] _preimage) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_FINALISETRANSFERTOOTHERBLOCKCHAIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment), 
                new org.web3j.abi.datatypes.generated.Bytes32(_preimage)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple6<String, String, BigInteger, byte[], BigInteger, BigInteger>> getInfo(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETINFO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple6<String, String, BigInteger, byte[], BigInteger, BigInteger>>(function,
                new Callable<Tuple6<String, String, BigInteger, byte[], BigInteger, BigInteger>>() {
                    @Override
                    public Tuple6<String, String, BigInteger, byte[], BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, String, BigInteger, byte[], BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (byte[]) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Boolean> isAllowedToken(String _tokenContract) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISALLOWEDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenContract)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> newTransferToOtherBlockchain(String _tokenContract, BigInteger _amount, byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_NEWTRANSFERTOOTHERBLOCKCHAIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenContract), 
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

    public RemoteFunctionCall<TransactionReceipt> refundTransferToOtherBlockchain(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REFUNDTRANSFERTOOTHERBLOCKCHAIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeAllowedToken(String _tokenContract) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEALLOWEDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenContract)), 
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

    public RemoteFunctionCall<BigInteger> transferState(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TRANSFERSTATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static Erc20HtlcTransfer load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Erc20HtlcTransfer(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Erc20HtlcTransfer load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Erc20HtlcTransfer(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Erc20HtlcTransfer load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Erc20HtlcTransfer(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Erc20HtlcTransfer load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Erc20HtlcTransfer(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Erc20HtlcTransfer> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger _timeLock) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_timeLock)));
        return deployRemoteCall(Erc20HtlcTransfer.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Erc20HtlcTransfer> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _timeLock) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_timeLock)));
        return deployRemoteCall(Erc20HtlcTransfer.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Erc20HtlcTransfer> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _timeLock) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_timeLock)));
        return deployRemoteCall(Erc20HtlcTransfer.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Erc20HtlcTransfer> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _timeLock) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_timeLock)));
        return deployRemoteCall(Erc20HtlcTransfer.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class AllowedTokenAddedEventResponse extends BaseEventResponse {
        public String tokenContract;
    }

    public static class AllowedTokenRemovedEventResponse extends BaseEventResponse {
        public String tokenContract;
    }

    public static class TransferCompletedEventResponse extends BaseEventResponse {
        public byte[] commitment;
    }

    public static class TransferInitEventResponse extends BaseEventResponse {
        public byte[] commitment;

        public String sender;

        public String tokenContract;

        public BigInteger amount;

        public BigInteger timeLock;
    }

    public static class TransferRefundedEventResponse extends BaseEventResponse {
        public byte[] commitment;
    }
}
