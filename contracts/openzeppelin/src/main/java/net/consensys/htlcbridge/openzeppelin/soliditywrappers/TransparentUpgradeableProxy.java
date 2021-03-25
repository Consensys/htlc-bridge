package net.consensys.htlcbridge.openzeppelin.soliditywrappers;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
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
public class TransparentUpgradeableProxy extends Contract {
    public static final String BINARY = "608060405260405162000da038038062000da0833981016040819052620000269162000335565b82816200005560017f360894a13ba1a3210667c828492db98dca3e2076cc3735a920a3ca505d382bbd62000464565b60008051602062000d59833981519152146200008157634e487b7160e01b600052600160045260246000fd5b6200008c826200012a565b805115620000ad57620000ab8282620001c060201b620003c41760201c565b505b50620000dd905060017fb53127684a568b3173ae13b9f8a6016e243e63b6e8ee1178d6a717850b5d610462000464565b60008051602062000d39833981519152146200010957634e487b7160e01b600052600160045260246000fd5b620001218260008051602062000d3983398151915255565b505050620004d1565b6200014081620001ef60201b620003f01760201c565b620001ad5760405162461bcd60e51b815260206004820152603260248201527f4552433139363750726f78793a206e657720696d706c656d656e746174696f6e604482015271081a5cc81b9bdd08184818dbdb9d1c9858dd60721b60648201526084015b60405180910390fd5b60008051602062000d5983398151915255565b6060620001e8838360405180606001604052806027815260200162000d7960279139620001f9565b9392505050565b803b15155b919050565b60606200020684620001ef565b620002635760405162461bcd60e51b815260206004820152602660248201527f416464726573733a2064656c65676174652063616c6c20746f206e6f6e2d636f6044820152651b9d1c9858dd60d21b6064820152608401620001a4565b600080856001600160a01b03168560405162000280919062000411565b600060405180830381855af49150503d8060008114620002bd576040519150601f19603f3d011682016040523d82523d6000602084013e620002c2565b606091505b509092509050620002d5828286620002df565b9695505050505050565b60608315620002f0575081620001e8565b825115620003015782518084602001fd5b8160405162461bcd60e51b8152600401620001a491906200042f565b80516001600160a01b0381168114620001f457600080fd5b6000806000606084860312156200034a578283fd5b62000355846200031d565b925062000365602085016200031d565b60408501519092506001600160401b038082111562000382578283fd5b818601915086601f83011262000396578283fd5b815181811115620003ab57620003ab620004bb565b604051601f8201601f19908116603f01168101908382118183101715620003d657620003d6620004bb565b81604052828152896020848701011115620003ef578586fd5b6200040283602083016020880162000488565b80955050505050509250925092565b600082516200042581846020870162000488565b9190910192915050565b60006020825282518060208401526200045081604085016020870162000488565b601f01601f19169190910160400192915050565b6000828210156200048357634e487b7160e01b81526011600452602481fd5b500390565b60005b83811015620004a55781810151838201526020016200048b565b83811115620004b5576000848401525b50505050565b634e487b7160e01b600052604160045260246000fd5b61085880620004e16000396000f3fe60806040526004361061004e5760003560e01c80633659cfe6146100655780634f1ef286146100855780635c60da1b146100985780638f283970146100c9578063f851a440146100e95761005d565b3661005d5761005b6100fe565b005b61005b6100fe565b34801561007157600080fd5b5061005b6100803660046106c4565b610138565b61005b6100933660046106de565b61017a565b3480156100a457600080fd5b506100ad6101ff565b6040516001600160a01b03909116815260200160405180910390f35b3480156100d557600080fd5b5061005b6100e43660046106c4565b610265565b3480156100f557600080fd5b506100ad61037e565b6101066103fa565b6101366101317f360894a13ba1a3210667c828492db98dca3e2076cc3735a920a3ca505d382bbc5490565b6104a1565b565b6000805160206107dc833981519152546001600160a01b0316336001600160a01b0316141561016f5761016a816104c5565b610177565b6101776100fe565b50565b6000805160206107dc833981519152546001600160a01b0316336001600160a01b031614156101f2576101ac836104c5565b6101ec8383838080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152506103c492505050565b506101fa565b6101fa6100fe565b505050565b60006102176000805160206107dc8339815191525490565b6001600160a01b0316336001600160a01b0316141561025a577f360894a13ba1a3210667c828492db98dca3e2076cc3735a920a3ca505d382bbc545b9050610262565b6102626100fe565b90565b6000805160206107dc833981519152546001600160a01b0316336001600160a01b0316141561016f576001600160a01b03811661030f5760405162461bcd60e51b815260206004820152603a60248201527f5472616e73706172656e745570677261646561626c6550726f78793a206e657760448201527f2061646d696e20697320746865207a65726f206164647265737300000000000060648201526084015b60405180910390fd5b7f7e644d79422f17c01e4894b5f4f588d331ebfa28653d42ae832dc59e38c9798f6103466000805160206107dc8339815191525490565b604080516001600160a01b03928316815291841660208301520160405180910390a161016a816000805160206107dc83398151915255565b60006103966000805160206107dc8339815191525490565b6001600160a01b0316336001600160a01b0316141561025a576000805160206107dc83398151915254610253565b60606103e983836040518060600160405280602781526020016107fc60279139610505565b9392505050565b803b15155b919050565b6000805160206107dc833981519152546001600160a01b0316336001600160a01b0316141561049c5760405162461bcd60e51b815260206004820152604260248201527f5472616e73706172656e745570677261646561626c6550726f78793a2061646d60448201527f696e2063616e6e6f742066616c6c6261636b20746f2070726f78792074617267606482015261195d60f21b608482015260a401610306565b610136565b3660008037600080366000845af43d6000803e8080156104c0573d6000f35b3d6000fd5b6104ce816105e0565b6040516001600160a01b038216907fbc7cd75a20ee27fd9adebab32041f755214dbc6bffa90cc0225b39da2e5c2d3b90600090a250565b6060610510846103f0565b61056b5760405162461bcd60e51b815260206004820152602660248201527f416464726573733a2064656c65676174652063616c6c20746f206e6f6e2d636f6044820152651b9d1c9858dd60d21b6064820152608401610306565b600080856001600160a01b031685604051610586919061075c565b600060405180830381855af49150503d80600081146105c1576040519150601f19603f3d011682016040523d82523d6000602084013e6105c6565b606091505b50915091506105d6828286610674565b9695505050505050565b6105e9816103f0565b6106505760405162461bcd60e51b815260206004820152603260248201527f4552433139363750726f78793a206e657720696d706c656d656e746174696f6e604482015271081a5cc81b9bdd08184818dbdb9d1c9858dd60721b6064820152608401610306565b7f360894a13ba1a3210667c828492db98dca3e2076cc3735a920a3ca505d382bbc55565b606083156106835750816103e9565b8251156106935782518084602001fd5b8160405162461bcd60e51b81526004016103069190610778565b80356001600160a01b03811681146103f557600080fd5b6000602082840312156106d5578081fd5b6103e9826106ad565b6000806000604084860312156106f2578182fd5b6106fb846106ad565b9250602084013567ffffffffffffffff80821115610717578384fd5b818601915086601f83011261072a578384fd5b813581811115610738578485fd5b876020828501011115610749578485fd5b6020830194508093505050509250925092565b6000825161076e8184602087016107ab565b9190910192915050565b60006020825282518060208401526107978160408501602087016107ab565b601f01601f19169190910160400192915050565b60005b838110156107c65781810151838201526020016107ae565b838111156107d5576000848401525b5050505056feb53127684a568b3173ae13b9f8a6016e243e63b6e8ee1178d6a717850b5d6103416464726573733a206c6f772d6c6576656c2064656c65676174652063616c6c206661696c6564a2646970667358221220df055e0640c21067ea38d46fc9fefd0bc5efef42362517d44426d71d3474fcdc64736f6c63430008020033b53127684a568b3173ae13b9f8a6016e243e63b6e8ee1178d6a717850b5d6103360894a13ba1a3210667c828492db98dca3e2076cc3735a920a3ca505d382bbc416464726573733a206c6f772d6c6576656c2064656c65676174652063616c6c206661696c6564";

    public static final String FUNC_ADMIN = "admin";

    public static final String FUNC_CHANGEADMIN = "changeAdmin";

    public static final String FUNC_IMPLEMENTATION = "implementation";

    public static final String FUNC_UPGRADETO = "upgradeTo";

    public static final String FUNC_UPGRADETOANDCALL = "upgradeToAndCall";

    public static final Event ADMINCHANGED_EVENT = new Event("AdminChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event UPGRADED_EVENT = new Event("Upgraded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected TransparentUpgradeableProxy(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TransparentUpgradeableProxy(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TransparentUpgradeableProxy(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TransparentUpgradeableProxy(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<AdminChangedEventResponse> getAdminChangedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADMINCHANGED_EVENT, transactionReceipt);
        ArrayList<AdminChangedEventResponse> responses = new ArrayList<AdminChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AdminChangedEventResponse typedResponse = new AdminChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousAdmin = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newAdmin = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AdminChangedEventResponse> adminChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AdminChangedEventResponse>() {
            @Override
            public AdminChangedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADMINCHANGED_EVENT, log);
                AdminChangedEventResponse typedResponse = new AdminChangedEventResponse();
                typedResponse.log = log;
                typedResponse.previousAdmin = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newAdmin = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AdminChangedEventResponse> adminChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADMINCHANGED_EVENT));
        return adminChangedEventFlowable(filter);
    }

    public List<UpgradedEventResponse> getUpgradedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(UPGRADED_EVENT, transactionReceipt);
        ArrayList<UpgradedEventResponse> responses = new ArrayList<UpgradedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UpgradedEventResponse typedResponse = new UpgradedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.implementation = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<UpgradedEventResponse> upgradedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, UpgradedEventResponse>() {
            @Override
            public UpgradedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(UPGRADED_EVENT, log);
                UpgradedEventResponse typedResponse = new UpgradedEventResponse();
                typedResponse.log = log;
                typedResponse.implementation = (String) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<UpgradedEventResponse> upgradedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UPGRADED_EVENT));
        return upgradedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> admin() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADMIN, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> changeAdmin(String newAdmin) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CHANGEADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newAdmin)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> implementation() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_IMPLEMENTATION, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> upgradeTo(String newImplementation) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UPGRADETO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newImplementation)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> upgradeToAndCall(String newImplementation, byte[] data) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UPGRADETOANDCALL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newImplementation), 
                new org.web3j.abi.datatypes.DynamicBytes(data)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static TransparentUpgradeableProxy load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TransparentUpgradeableProxy(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TransparentUpgradeableProxy load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TransparentUpgradeableProxy(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TransparentUpgradeableProxy load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new TransparentUpgradeableProxy(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TransparentUpgradeableProxy load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TransparentUpgradeableProxy(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TransparentUpgradeableProxy> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _logic, String admin_, byte[] _data) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _logic), 
                new org.web3j.abi.datatypes.Address(160, admin_), 
                new org.web3j.abi.datatypes.DynamicBytes(_data)));
        return deployRemoteCall(TransparentUpgradeableProxy.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<TransparentUpgradeableProxy> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _logic, String admin_, byte[] _data) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _logic), 
                new org.web3j.abi.datatypes.Address(160, admin_), 
                new org.web3j.abi.datatypes.DynamicBytes(_data)));
        return deployRemoteCall(TransparentUpgradeableProxy.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<TransparentUpgradeableProxy> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _logic, String admin_, byte[] _data) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _logic), 
                new org.web3j.abi.datatypes.Address(160, admin_), 
                new org.web3j.abi.datatypes.DynamicBytes(_data)));
        return deployRemoteCall(TransparentUpgradeableProxy.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<TransparentUpgradeableProxy> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _logic, String admin_, byte[] _data) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _logic), 
                new org.web3j.abi.datatypes.Address(160, admin_), 
                new org.web3j.abi.datatypes.DynamicBytes(_data)));
        return deployRemoteCall(TransparentUpgradeableProxy.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class AdminChangedEventResponse extends BaseEventResponse {
        public String previousAdmin;

        public String newAdmin;
    }

    public static class UpgradedEventResponse extends BaseEventResponse {
        public String implementation;
    }
}
