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
public class Erc20HtlcTransfer extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b5061171c806100206000396000f3fe608060405234801561001057600080fd5b50600436106101da5760003560e01c80635f87fba911610104578063b6f74cd6116100a2578063c99c538f11610071578063c99c538f14610488578063ce09a9dc146104d3578063e8582468146104dc578063eb2838c314610535576101da565b8063b6f74cd614610412578063b9c136ec14610425578063bddf94c114610438578063be821cd41461045d576101da565b8063700b5968116100de578063700b5968146103c657806372253f11146103d9578063b043852d146103ec578063b1f3dc84146103ff576101da565b80635f87fba9146103745780636747b1e91461038757806369409bb01461039a576101da565b80632d34b0ab1161017c5780634db0022e1161014b5780634db0022e1461032d5780634e95b54e1461034457806350f034241461035757806354fd4d501461036a576101da565b80632d34b0ab146102bf57806332413c90146102e25780633541c2a2146102f55780634ca96f4b1461031a576101da565b806317b56fd3116101b857806317b56fd31461022f5780631d20dad914610242578063256d464e1461026557806328af0c8a14610291576101da565b80630bb44109146101df5780630c8d3698146101f45780630d0e6e7214610207575b600080fd5b6101f26101ed3660046114bc565b610576565b005b6101f26102023660046115ba565b6105d2565b61021a6102153660046115a2565b6106a3565b60405190151581526020015b60405180910390f35b6101f261023d3660046115a2565b6106c3565b61021a6102503660046114bc565b600f6020526000908152604090205460ff1681565b61021a6102733660046114bc565b6001600160a01b03166000908152600f602052604090205460ff1690565b61021a61029f3660046114bc565b6001600160a01b0390811660009081526001602052604090205416151590565b61021a6102cd3660046114bc565b60026020526000908152604090205460ff1681565b6101f26102f03660046114bc565b6108aa565b61021a6103033660046115a2565b600090815260106020526040902060050154421190565b6101f2610328366004611550565b610937565b61033660005481565b604051908152602001610226565b61021a6103523660046115ba565b610bcf565b6101f26103653660046114bc565b610c04565b630c0a6ed5610336565b6101f261038236600461150f565b610c55565b6101f26103953660046115ba565b610edf565b61021a6103a83660046114bc565b6001600160a01b031660009081526002602052604090205460ff1690565b6103366103d43660046115a2565b611108565b6101f26103e73660046114bc565b61113f565b6101f26103fa3660046114dd565b61118f565b6101f261040d3660046114bc565b611229565b6103366104203660046115a2565b61127d565b6101f26104333660046115ba565b6112b4565b61021a6104463660046115a2565b600090815260036020526040902060060154421190565b61021a61046b3660046115a2565b6000908152601060205260409020546001600160a01b0316151590565b61049b6104963660046115a2565b611393565b604080516001600160a01b039788168152969095166020870152938501929092526060840152608083015260a082015260c001610226565b610336600e5481565b6104ef6104ea3660046115a2565b611421565b604080516001600160a01b039889168152968816602088015294909616938501939093526060840191909152608083015260a082015260c081019190915260e001610226565b61055e6105433660046114bc565b6001602052600090815260409020546001600160a01b031681565b6040516001600160a01b039091168152602001610226565b3360009081526002602052604090205460ff166105ae5760405162461bcd60e51b81526004016105a590611612565b60405180910390fd5b6001600160a01b03166000908152600260205260409020805460ff19166001179055565b6000828152601060205260409020546001600160a01b03166106065760405162461bcd60e51b81526004016105a59061168b565b6106108282610bcf565b61062c5760405162461bcd60e51b81526004016105a590611649565b6000828152601060205260409020600601541561065b5760405162461bcd60e51b81526004016105a5906115db565b6000828152601060205260408082206004810184905560026006909101555183917f83da23f35f5fd0d467a1dde4a2a7c2794d4640b6504c8a32c5a87b047796367e91a25050565b6000818152600360205260409020546001600160a01b031615155b919050565b6000818152601060205260409020546001600160a01b03166106f75760405162461bcd60e51b81526004016105a59061168b565b600081815260106020526040902060060154156107265760405162461bcd60e51b81526004016105a5906115db565b60008181526010602052604090206005015442116107905760405162461bcd60e51b815260206004820152602160248201527f5472616e73616374696f6e20686173206e6f74207965742074696d65642d6f756044820152601d60fa1b60648201526084016105a5565b6000818152601060205260409081902060018101548154600290920154925163a9059cbb60e01b81526001600160a01b0392831660048201526024810193909352169063a9059cbb90604401602060405180830381600087803b1580156107f657600080fd5b505af115801561080a573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061082e9190611582565b61086a5760405162461bcd60e51b815260206004820152600d60248201526c1c99599d5b990819985a5b1959609a1b60448201526064016105a5565b60008181526010602052604080822060016006909101555182917fb4533ebb723117aaa2826214b6c86a33ab251ad1095b054f04c1679e300bdaa991a250565b3360009081526002602052604090205460ff166108d95760405162461bcd60e51b81526004016105a590611612565b6001600160a01b03811660008181526001602090815260409182902080546001600160a01b031916905590519182527f3ad52e1d360df277e254b4e5c9cbae79ea87778c8b07758e9311981b66333e1d91015b60405180910390a150565b6000818152601060205260409020546001600160a01b0316156109965760405162461bcd60e51b81526020600482015260176024820152765472616e7366657220616c72656164792065786973747360481b60448201526064016105a5565b6001600160a01b0383166000908152600f602052604090205460ff166109f75760405162461bcd60e51b8152602060048201526016602482015275546f6b656e206e6f74207472616e7366657261626c6560501b60448201526064016105a5565b6040516323b872dd60e01b8152336004820152306024820152604481018390526001600160a01b038416906323b872dd90606401602060405180830381600087803b158015610a4557600080fd5b505af1158015610a59573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610a7d9190611582565b610abb5760405162461bcd60e51b815260206004820152600f60248201526e1d1c985b9cd9995c8819985a5b1959608a1b60448201526064016105a5565b6000600e5442610acb91906116c2565b6040805160e081018252338082526001600160a01b0388811660208085019182528486018a8152606086018a815260006080880181815260a089018b815260c08a018381528e84526010909652918a9020985189549088166001600160a01b0319918216178a55955160018a018054919098169616959095179095559051600287015551600386015590516004850155905160058401555160069092019190915590519192509083907f470a379f66a6eef3070bd979b98c2dd6d7bfb668e17c67819722028655d5406190610bc1908890889087906001600160a01b039390931683526020830191909152604082015260600190565b60405180910390a350505050565b600081604051602001610be491815260200190565b604051602081830303815290604052805190602001208314905092915050565b6001600160a01b0381166000818152600f6020908152604091829020805460ff1916905590519182527fce011ecaa68a6adc53c64d97441c1e5d0d818b4589fb0576303032c2aef963f6910161092c565b3360009081526002602052604090205460ff16610c845760405162461bcd60e51b81526004016105a590611612565b610c8d816106a3565b15610cd45760405162461bcd60e51b81526020600482015260176024820152765472616e7366657220616c72656164792065786973747360481b60448201526064016105a5565b6001600160a01b0380851660009081526001602052604090205416610d345760405162461bcd60e51b8152602060048201526016602482015275546f6b656e206e6f74207472616e7366657261626c6560501b60448201526064016105a5565b60008054610d4290426116c2565b9050604051806101000160405280336001600160a01b03168152602001856001600160a01b03168152602001866001600160a01b031681526020018481526020018381526020016000801b815260200182815260200160008152506003600084815260200190815260200160002060008201518160000160006101000a8154816001600160a01b0302191690836001600160a01b0316021790555060208201518160010160006101000a8154816001600160a01b0302191690836001600160a01b0316021790555060408201518160020160006101000a8154816001600160a01b0302191690836001600160a01b03160217905550606082015181600301556080820151816004015560a0820151816005015560c0820151816006015560e08201518160070155905050836001600160a01b0316336001600160a01b0316837f509a22359cd26b11a918295a1a78f9636ee8bef0e1252f3da5c631dee00c3693888786604051610ed0939291906001600160a01b039390931683526020830191909152604082015260600190565b60405180910390a45050505050565b610ee8826106a3565b610f045760405162461bcd60e51b81526004016105a59061168b565b610f0e8282610bcf565b610f2a5760405162461bcd60e51b81526004016105a590611649565b60008281526003602052604090206007015415610f595760405162461bcd60e51b81526004016105a5906115db565b600082815260036020526040902060060154421115610faf5760405162461bcd60e51b8152602060048201526012602482015271151c985b9cd9995c881d1a5b59590b5bdd5d60721b60448201526064016105a5565b600082815260036020818152604080842060028101546001600160a01b03908116808752600180865284882054978a905294869052938201549190940154915163a9059cbb60e01b815290841660048201526024810191909152909290911690819063a9059cbb90604401602060405180830381600087803b15801561103457600080fd5b505af1158015611048573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061106c9190611582565b6110a95760405162461bcd60e51b815260206004820152600e60248201526d1d1c985b99995c8819985a5b195960921b60448201526064016105a5565b600084815260036020526040908190206005810185905560026007909101555184907fa70eb85bac87b136d136ad64b8d72ca165c84dcc438369df296bf4fddfb4a254906110fa9086815260200190565b60405180910390a250505050565b600081815260106020526040812060050154421115611129575060036106be565b5060009081526010602052604090206006015490565b3360009081526002602052604090205460ff1661116e5760405162461bcd60e51b81526004016105a590611612565b6001600160a01b03166000908152600260205260409020805460ff19169055565b3360009081526002602052604090205460ff166111be5760405162461bcd60e51b81526004016105a590611612565b6001600160a01b0382811660008181526001602090815260409182902080546001600160a01b031916948616948517905581519283528201929092527f3416532c07455f30acfd3ba117394d859f7a84cb6e7d21d3c47909e7bf4bf480910160405180910390a15050565b6001600160a01b0381166000818152600f6020908152604091829020805460ff1916600117905590519182527fe09ab33dfca3a4f10512787bd5fdb83b7ac6fc4dfbb99ebac9e22d01f18de853910161092c565b60008181526003602052604081206006015442111561129e575060036106be565b5060009081526003602052604090206007015490565b601b54610100900460ff16806112cd5750601b5460ff16155b6113305760405162461bcd60e51b815260206004820152602e60248201527f496e697469616c697a61626c653a20636f6e747261637420697320616c72656160448201526d191e481a5b9a5d1a585b1a5e995960921b60648201526084016105a5565b601b54610100900460ff1615801561135b57601b805460ff1961ff0019909116610100171660011790555b600e8390556000828155338152600260205260409020805460ff19166001179055801561138e57601b805461ff00191690555b505050565b6000806000806000806113bd876000908152601060205260409020546001600160a01b0316151590565b6113d95760405162461bcd60e51b81526004016105a59061168b565b5050506000938452505060106020525060409020805460018201546002830154600484015460058501546006909501546001600160a01b039485169694909316949193909290565b6000806000806000806000611435886106a3565b6114515760405162461bcd60e51b81526004016105a59061168b565b5050506000948552505060036020819052604090932080546001820154600283015495830154600584015460068501546007909501546001600160a01b039485169993851698949094169650909450929190565b80356001600160a01b03811681146106be57600080fd5b6000602082840312156114cd578081fd5b6114d6826114a5565b9392505050565b600080604083850312156114ef578081fd5b6114f8836114a5565b9150611506602084016114a5565b90509250929050565b60008060008060808587031215611524578182fd5b61152d856114a5565b935061153b602086016114a5565b93969395505050506040820135916060013590565b600080600060608486031215611564578283fd5b61156d846114a5565b95602085013595506040909401359392505050565b600060208284031215611593578081fd5b815180151581146114d6578182fd5b6000602082840312156115b3578081fd5b5035919050565b600080604083850312156115cc578182fd5b50508035926020909101359150565b6020808252601a908201527f5472616e73666572206e6f7420696e206f70656e207374617465000000000000604082015260600190565b60208082526019908201527f4e6f7420616e20617574686f72697365642072656c6179657200000000000000604082015260600190565b60208082526022908201527f507265696d61676520646f6573206e6f74206d6174636820636f6d6d69746d656040820152611b9d60f21b606082015260800190565b60208082526017908201527f5472616e7366657220646f6573206e6f74206578697374000000000000000000604082015260600190565b600082198211156116e157634e487b7160e01b81526011600452602481fd5b50019056fea2646970667358221220949d5bc543953c052669936f769ad170ecc80dc6ae5c0ba9e150970554d2dd2364736f6c63430008020033";

    public static final String FUNC_ADDAUTHORISEDRELAYER = "addAuthorisedRelayer";

    public static final String FUNC_ADDDESTALLOWEDTOKEN = "addDestAllowedToken";

    public static final String FUNC_ADDSOURCEALLOWEDTOKEN = "addSourceAllowedToken";

    public static final String FUNC_AUTHORISEDRELAYER = "authorisedRelayer";

    public static final String FUNC_DESTALLOWEDTOKENS = "destAllowedTokens";

    public static final String FUNC_DESTTIMELOCKPERIOD = "destTimeLockPeriod";

    public static final String FUNC_DESTTRANSFEREXISTS = "destTransferExists";

    public static final String FUNC_DESTTRANSFEREXPIRED = "destTransferExpired";

    public static final String FUNC_DESTTRANSFERSTATE = "destTransferState";

    public static final String FUNC_FINALISETRANSFERFROMOTHERBLOCKCHAIN = "finaliseTransferFromOtherBlockchain";

    public static final String FUNC_FINALISETRANSFERTOOTHERBLOCKCHAIN = "finaliseTransferToOtherBlockchain";

    public static final String FUNC_GETDESTINFO = "getDestInfo";

    public static final String FUNC_GETSOURCEINFO = "getSourceInfo";

    public static final String FUNC_INITIALISE = "initialise";

    public static final String FUNC_ISAUTHORISEDRELAYER = "isAuthorisedRelayer";

    public static final String FUNC_ISDESTALLOWEDTOKEN = "isDestAllowedToken";

    public static final String FUNC_ISSOURCEALLOWEDTOKEN = "isSourceAllowedToken";

    public static final String FUNC_NEWTRANSFERFROMOTHERBLOCKCHAIN = "newTransferFromOtherBlockchain";

    public static final String FUNC_NEWTRANSFERTOOTHERBLOCKCHAIN = "newTransferToOtherBlockchain";

    public static final String FUNC_PREIMAGEMATCHESCOMMITMENT = "preimageMatchesCommitment";

    public static final String FUNC_REFUNDTRANSFERTOOTHERBLOCKCHAIN = "refundTransferToOtherBlockchain";

    public static final String FUNC_REMOVEAUTHORISEDRELAYER = "removeAuthorisedRelayer";

    public static final String FUNC_REMOVEDESTALLOWEDTOKEN = "removeDestAllowedToken";

    public static final String FUNC_REMOVESOURCEALLOWEDTOKEN = "removeSourceAllowedToken";

    public static final String FUNC_SOURCEALLOWEDTOKENS = "sourceAllowedTokens";

    public static final String FUNC_SOURCETIMELOCKPERIOD = "sourceTimeLockPeriod";

    public static final String FUNC_SOURCETRANSFEREXISTS = "sourceTransferExists";

    public static final String FUNC_SOURCETRANSFEREXPIRED = "sourceTransferExpired";

    public static final String FUNC_SOURCETRANSFERSTATE = "sourceTransferState";

    public static final String FUNC_VERSION = "version";

    public static final Event DESTALLOWEDTOKENADDED_EVENT = new Event("DestAllowedTokenAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event DESTALLOWEDTOKENREMOVED_EVENT = new Event("DestAllowedTokenRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event DESTTRANSFERCOMPLETED_EVENT = new Event("DestTransferCompleted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>() {}));
    ;

    public static final Event DESTTRANSFERINIT_EVENT = new Event("DestTransferInit", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event DESTTRANSFERREFUNDED_EVENT = new Event("DestTransferRefunded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}));
    ;

    public static final Event SOURCEALLOWEDTOKENADDED_EVENT = new Event("SourceAllowedTokenAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event SOURCEALLOWEDTOKENREMOVED_EVENT = new Event("SourceAllowedTokenRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event SOURCETRANSFERCOMPLETED_EVENT = new Event("SourceTransferCompleted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}));
    ;

    public static final Event SOURCETRANSFERINIT_EVENT = new Event("SourceTransferInit", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event SOURCETRANSFERREFUNDED_EVENT = new Event("SourceTransferRefunded", 
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

    public List<DestAllowedTokenAddedEventResponse> getDestAllowedTokenAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DESTALLOWEDTOKENADDED_EVENT, transactionReceipt);
        ArrayList<DestAllowedTokenAddedEventResponse> responses = new ArrayList<DestAllowedTokenAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DestAllowedTokenAddedEventResponse typedResponse = new DestAllowedTokenAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.otherBlockchainTokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.thisBlockchainTokenContract = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DestAllowedTokenAddedEventResponse> destAllowedTokenAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DestAllowedTokenAddedEventResponse>() {
            @Override
            public DestAllowedTokenAddedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DESTALLOWEDTOKENADDED_EVENT, log);
                DestAllowedTokenAddedEventResponse typedResponse = new DestAllowedTokenAddedEventResponse();
                typedResponse.log = log;
                typedResponse.otherBlockchainTokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.thisBlockchainTokenContract = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DestAllowedTokenAddedEventResponse> destAllowedTokenAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DESTALLOWEDTOKENADDED_EVENT));
        return destAllowedTokenAddedEventFlowable(filter);
    }

    public List<DestAllowedTokenRemovedEventResponse> getDestAllowedTokenRemovedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DESTALLOWEDTOKENREMOVED_EVENT, transactionReceipt);
        ArrayList<DestAllowedTokenRemovedEventResponse> responses = new ArrayList<DestAllowedTokenRemovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DestAllowedTokenRemovedEventResponse typedResponse = new DestAllowedTokenRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DestAllowedTokenRemovedEventResponse> destAllowedTokenRemovedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DestAllowedTokenRemovedEventResponse>() {
            @Override
            public DestAllowedTokenRemovedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DESTALLOWEDTOKENREMOVED_EVENT, log);
                DestAllowedTokenRemovedEventResponse typedResponse = new DestAllowedTokenRemovedEventResponse();
                typedResponse.log = log;
                typedResponse.tokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DestAllowedTokenRemovedEventResponse> destAllowedTokenRemovedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DESTALLOWEDTOKENREMOVED_EVENT));
        return destAllowedTokenRemovedEventFlowable(filter);
    }

    public List<DestTransferCompletedEventResponse> getDestTransferCompletedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DESTTRANSFERCOMPLETED_EVENT, transactionReceipt);
        ArrayList<DestTransferCompletedEventResponse> responses = new ArrayList<DestTransferCompletedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DestTransferCompletedEventResponse typedResponse = new DestTransferCompletedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.commitment = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.preimage = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DestTransferCompletedEventResponse> destTransferCompletedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DestTransferCompletedEventResponse>() {
            @Override
            public DestTransferCompletedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DESTTRANSFERCOMPLETED_EVENT, log);
                DestTransferCompletedEventResponse typedResponse = new DestTransferCompletedEventResponse();
                typedResponse.log = log;
                typedResponse.commitment = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.preimage = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DestTransferCompletedEventResponse> destTransferCompletedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DESTTRANSFERCOMPLETED_EVENT));
        return destTransferCompletedEventFlowable(filter);
    }

    public List<DestTransferInitEventResponse> getDestTransferInitEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DESTTRANSFERINIT_EVENT, transactionReceipt);
        ArrayList<DestTransferInitEventResponse> responses = new ArrayList<DestTransferInitEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DestTransferInitEventResponse typedResponse = new DestTransferInitEventResponse();
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

    public Flowable<DestTransferInitEventResponse> destTransferInitEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DestTransferInitEventResponse>() {
            @Override
            public DestTransferInitEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DESTTRANSFERINIT_EVENT, log);
                DestTransferInitEventResponse typedResponse = new DestTransferInitEventResponse();
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

    public Flowable<DestTransferInitEventResponse> destTransferInitEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DESTTRANSFERINIT_EVENT));
        return destTransferInitEventFlowable(filter);
    }

    public List<DestTransferRefundedEventResponse> getDestTransferRefundedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DESTTRANSFERREFUNDED_EVENT, transactionReceipt);
        ArrayList<DestTransferRefundedEventResponse> responses = new ArrayList<DestTransferRefundedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DestTransferRefundedEventResponse typedResponse = new DestTransferRefundedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.commitment = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DestTransferRefundedEventResponse> destTransferRefundedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DestTransferRefundedEventResponse>() {
            @Override
            public DestTransferRefundedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DESTTRANSFERREFUNDED_EVENT, log);
                DestTransferRefundedEventResponse typedResponse = new DestTransferRefundedEventResponse();
                typedResponse.log = log;
                typedResponse.commitment = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DestTransferRefundedEventResponse> destTransferRefundedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DESTTRANSFERREFUNDED_EVENT));
        return destTransferRefundedEventFlowable(filter);
    }

    public List<SourceAllowedTokenAddedEventResponse> getSourceAllowedTokenAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SOURCEALLOWEDTOKENADDED_EVENT, transactionReceipt);
        ArrayList<SourceAllowedTokenAddedEventResponse> responses = new ArrayList<SourceAllowedTokenAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SourceAllowedTokenAddedEventResponse typedResponse = new SourceAllowedTokenAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SourceAllowedTokenAddedEventResponse> sourceAllowedTokenAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SourceAllowedTokenAddedEventResponse>() {
            @Override
            public SourceAllowedTokenAddedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SOURCEALLOWEDTOKENADDED_EVENT, log);
                SourceAllowedTokenAddedEventResponse typedResponse = new SourceAllowedTokenAddedEventResponse();
                typedResponse.log = log;
                typedResponse.tokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SourceAllowedTokenAddedEventResponse> sourceAllowedTokenAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SOURCEALLOWEDTOKENADDED_EVENT));
        return sourceAllowedTokenAddedEventFlowable(filter);
    }

    public List<SourceAllowedTokenRemovedEventResponse> getSourceAllowedTokenRemovedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SOURCEALLOWEDTOKENREMOVED_EVENT, transactionReceipt);
        ArrayList<SourceAllowedTokenRemovedEventResponse> responses = new ArrayList<SourceAllowedTokenRemovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SourceAllowedTokenRemovedEventResponse typedResponse = new SourceAllowedTokenRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SourceAllowedTokenRemovedEventResponse> sourceAllowedTokenRemovedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SourceAllowedTokenRemovedEventResponse>() {
            @Override
            public SourceAllowedTokenRemovedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SOURCEALLOWEDTOKENREMOVED_EVENT, log);
                SourceAllowedTokenRemovedEventResponse typedResponse = new SourceAllowedTokenRemovedEventResponse();
                typedResponse.log = log;
                typedResponse.tokenContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SourceAllowedTokenRemovedEventResponse> sourceAllowedTokenRemovedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SOURCEALLOWEDTOKENREMOVED_EVENT));
        return sourceAllowedTokenRemovedEventFlowable(filter);
    }

    public List<SourceTransferCompletedEventResponse> getSourceTransferCompletedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SOURCETRANSFERCOMPLETED_EVENT, transactionReceipt);
        ArrayList<SourceTransferCompletedEventResponse> responses = new ArrayList<SourceTransferCompletedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SourceTransferCompletedEventResponse typedResponse = new SourceTransferCompletedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.commitment = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SourceTransferCompletedEventResponse> sourceTransferCompletedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SourceTransferCompletedEventResponse>() {
            @Override
            public SourceTransferCompletedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SOURCETRANSFERCOMPLETED_EVENT, log);
                SourceTransferCompletedEventResponse typedResponse = new SourceTransferCompletedEventResponse();
                typedResponse.log = log;
                typedResponse.commitment = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SourceTransferCompletedEventResponse> sourceTransferCompletedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SOURCETRANSFERCOMPLETED_EVENT));
        return sourceTransferCompletedEventFlowable(filter);
    }

    public List<SourceTransferInitEventResponse> getSourceTransferInitEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SOURCETRANSFERINIT_EVENT, transactionReceipt);
        ArrayList<SourceTransferInitEventResponse> responses = new ArrayList<SourceTransferInitEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SourceTransferInitEventResponse typedResponse = new SourceTransferInitEventResponse();
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

    public Flowable<SourceTransferInitEventResponse> sourceTransferInitEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SourceTransferInitEventResponse>() {
            @Override
            public SourceTransferInitEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SOURCETRANSFERINIT_EVENT, log);
                SourceTransferInitEventResponse typedResponse = new SourceTransferInitEventResponse();
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

    public Flowable<SourceTransferInitEventResponse> sourceTransferInitEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SOURCETRANSFERINIT_EVENT));
        return sourceTransferInitEventFlowable(filter);
    }

    public List<SourceTransferRefundedEventResponse> getSourceTransferRefundedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SOURCETRANSFERREFUNDED_EVENT, transactionReceipt);
        ArrayList<SourceTransferRefundedEventResponse> responses = new ArrayList<SourceTransferRefundedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SourceTransferRefundedEventResponse typedResponse = new SourceTransferRefundedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.commitment = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SourceTransferRefundedEventResponse> sourceTransferRefundedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SourceTransferRefundedEventResponse>() {
            @Override
            public SourceTransferRefundedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SOURCETRANSFERREFUNDED_EVENT, log);
                SourceTransferRefundedEventResponse typedResponse = new SourceTransferRefundedEventResponse();
                typedResponse.log = log;
                typedResponse.commitment = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SourceTransferRefundedEventResponse> sourceTransferRefundedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SOURCETRANSFERREFUNDED_EVENT));
        return sourceTransferRefundedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addAuthorisedRelayer(String _newRelayer) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDAUTHORISEDRELAYER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _newRelayer)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addDestAllowedToken(String _otherBlockchainTokenContract, String _thisBlockchainTokenContract) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDDESTALLOWEDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _otherBlockchainTokenContract), 
                new org.web3j.abi.datatypes.Address(160, _thisBlockchainTokenContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addSourceAllowedToken(String _tokenContract) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDSOURCEALLOWEDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> authorisedRelayer(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_AUTHORISEDRELAYER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> destAllowedTokens(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DESTALLOWEDTOKENS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> destTimeLockPeriod() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DESTTIMELOCKPERIOD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> destTransferExists(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DESTTRANSFEREXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> destTransferExpired(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DESTTRANSFEREXPIRED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> destTransferState(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DESTTRANSFERSTATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> finaliseTransferFromOtherBlockchain(byte[] _commitment, byte[] _preimage) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_FINALISETRANSFERFROMOTHERBLOCKCHAIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment), 
                new org.web3j.abi.datatypes.generated.Bytes32(_preimage)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> finaliseTransferToOtherBlockchain(byte[] _commitment, byte[] _preimage) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_FINALISETRANSFERTOOTHERBLOCKCHAIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment), 
                new org.web3j.abi.datatypes.generated.Bytes32(_preimage)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple7<String, String, String, BigInteger, byte[], BigInteger, BigInteger>> getDestInfo(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETDESTINFO, 
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

    public RemoteFunctionCall<Tuple6<String, String, BigInteger, byte[], BigInteger, BigInteger>> getSourceInfo(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSOURCEINFO, 
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

    public RemoteFunctionCall<TransactionReceipt> initialise(BigInteger _sourceTimeLock, BigInteger _destTimeLock) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_INITIALISE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sourceTimeLock), 
                new org.web3j.abi.datatypes.generated.Uint256(_destTimeLock)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> isAuthorisedRelayer(String _relayer) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISAUTHORISEDRELAYER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _relayer)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isDestAllowedToken(String _tokenContract) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISDESTALLOWEDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenContract)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isSourceAllowedToken(String _tokenContract) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISSOURCEALLOWEDTOKEN, 
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

    public RemoteFunctionCall<TransactionReceipt> removeAuthorisedRelayer(String _relayerToBeRemoved) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEAUTHORISEDRELAYER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _relayerToBeRemoved)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeDestAllowedToken(String _otherBlockchainTokenContract) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEDESTALLOWEDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _otherBlockchainTokenContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeSourceAllowedToken(String _tokenContract) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVESOURCEALLOWEDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> sourceAllowedTokens(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SOURCEALLOWEDTOKENS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> sourceTimeLockPeriod() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SOURCETIMELOCKPERIOD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> sourceTransferExists(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SOURCETRANSFEREXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> sourceTransferExpired(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SOURCETRANSFEREXPIRED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> sourceTransferState(byte[] _commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SOURCETRANSFERSTATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> version() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_VERSION, 
                Arrays.<Type>asList(), 
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

    public static RemoteCall<Erc20HtlcTransfer> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Erc20HtlcTransfer.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Erc20HtlcTransfer> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Erc20HtlcTransfer.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Erc20HtlcTransfer> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Erc20HtlcTransfer.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Erc20HtlcTransfer> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Erc20HtlcTransfer.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class DestAllowedTokenAddedEventResponse extends BaseEventResponse {
        public String otherBlockchainTokenContract;

        public String thisBlockchainTokenContract;
    }

    public static class DestAllowedTokenRemovedEventResponse extends BaseEventResponse {
        public String tokenContract;
    }

    public static class DestTransferCompletedEventResponse extends BaseEventResponse {
        public byte[] commitment;

        public byte[] preimage;
    }

    public static class DestTransferInitEventResponse extends BaseEventResponse {
        public byte[] commitment;

        public String relayer;

        public String recipient;

        public String tokenContract;

        public BigInteger amount;

        public BigInteger timeLock;
    }

    public static class DestTransferRefundedEventResponse extends BaseEventResponse {
        public byte[] commitment;
    }

    public static class SourceAllowedTokenAddedEventResponse extends BaseEventResponse {
        public String tokenContract;
    }

    public static class SourceAllowedTokenRemovedEventResponse extends BaseEventResponse {
        public String tokenContract;
    }

    public static class SourceTransferCompletedEventResponse extends BaseEventResponse {
        public byte[] commitment;
    }

    public static class SourceTransferInitEventResponse extends BaseEventResponse {
        public byte[] commitment;

        public String sender;

        public String tokenContract;

        public BigInteger amount;

        public BigInteger timeLock;
    }

    public static class SourceTransferRefundedEventResponse extends BaseEventResponse {
        public byte[] commitment;
    }
}
