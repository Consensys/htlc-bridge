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
 * <p>Generated with web3j version 4.8.7-SNAPSHOT.
 */
@SuppressWarnings("rawtypes")
public class TransparentUpgradeableProxy extends Contract {
    public static final String BINARY = "608060405260405162000d8238038062000d8283398101604081905262000026916200035e565b82816200005560017f360894a13ba1a3210667c828492db98dca3e2076cc3735a920a3ca505d382bbd6200043e565b60008051602062000d3b8339815191521462000075576200007562000464565b620000808262000112565b805115620000a1576200009f8282620001a860201b620003ba1760201c565b505b50620000d1905060017fb53127684a568b3173ae13b9f8a6016e243e63b6e8ee1178d6a717850b5d61046200043e565b60008051602062000d1b83398151915214620000f157620000f162000464565b620001098260008051602062000d1b83398151915255565b505050620004cd565b6200012881620001d760201b620003e61760201c565b620001955760405162461bcd60e51b815260206004820152603260248201527f4552433139363750726f78793a206e657720696d706c656d656e746174696f6e604482015271081a5cc81b9bdd08184818dbdb9d1c9858dd60721b60648201526084015b60405180910390fd5b60008051602062000d3b83398151915255565b6060620001d0838360405180606001604052806027815260200162000d5b60279139620001dd565b9392505050565b3b151590565b6060833b6200023e5760405162461bcd60e51b815260206004820152602660248201527f416464726573733a2064656c65676174652063616c6c20746f206e6f6e2d636f6044820152651b9d1c9858dd60d21b60648201526084016200018c565b600080856001600160a01b0316856040516200025b91906200047a565b600060405180830381855af49150503d806000811462000298576040519150601f19603f3d011682016040523d82523d6000602084013e6200029d565b606091505b509092509050620002b0828286620002ba565b9695505050505050565b60608315620002cb575081620001d0565b825115620002dc5782518084602001fd5b8160405162461bcd60e51b81526004016200018c919062000498565b80516001600160a01b03811681146200031057600080fd5b919050565b634e487b7160e01b600052604160045260246000fd5b60005b83811015620003485781810151838201526020016200032e565b8381111562000358576000848401525b50505050565b6000806000606084860312156200037457600080fd5b6200037f84620002f8565b92506200038f60208501620002f8565b60408501519092506001600160401b0380821115620003ad57600080fd5b818601915086601f830112620003c257600080fd5b815181811115620003d757620003d762000315565b604051601f8201601f19908116603f0116810190838211818310171562000402576200040262000315565b816040528281528960208487010111156200041c57600080fd5b6200042f8360208301602088016200032b565b80955050505050509250925092565b6000828210156200045f57634e487b7160e01b600052601160045260246000fd5b500390565b634e487b7160e01b600052600160045260246000fd5b600082516200048e8184602087016200032b565b9190910192915050565b6020815260008251806020840152620004b98160408501602087016200032b565b601f01601f19169190910160400192915050565b61083e80620004dd6000396000f3fe60806040526004361061004e5760003560e01c80633659cfe6146100655780634f1ef286146100855780635c60da1b146100985780638f283970146100c9578063f851a440146100e95761005d565b3661005d5761005b6100fe565b005b61005b6100fe565b34801561007157600080fd5b5061005b6100803660046106a8565b610138565b61005b6100933660046106c3565b610175565b3480156100a457600080fd5b506100ad6101fa565b6040516001600160a01b03909116815260200160405180910390f35b3480156100d557600080fd5b5061005b6100e43660046106a8565b61025c565b3480156100f557600080fd5b506100ad610375565b6101066103ec565b6101366101317f360894a13ba1a3210667c828492db98dca3e2076cc3735a920a3ca505d382bbc5490565b61048e565b565b6000805160206107c2833981519152546001600160a01b0316336001600160a01b0316141561016d5761016a816104b2565b50565b61016a6100fe565b6000805160206107c2833981519152546001600160a01b0316336001600160a01b031614156101ed576101a7836104b2565b6101e78383838080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152506103ba92505050565b50505050565b6101f56100fe565b505050565b60006102126000805160206107c28339815191525490565b6001600160a01b0316336001600160a01b0316141561025157507f360894a13ba1a3210667c828492db98dca3e2076cc3735a920a3ca505d382bbc5490565b6102596100fe565b90565b6000805160206107c2833981519152546001600160a01b0316336001600160a01b0316141561016d576001600160a01b0381166103065760405162461bcd60e51b815260206004820152603a60248201527f5472616e73706172656e745570677261646561626c6550726f78793a206e657760448201527f2061646d696e20697320746865207a65726f206164647265737300000000000060648201526084015b60405180910390fd5b7f7e644d79422f17c01e4894b5f4f588d331ebfa28653d42ae832dc59e38c9798f61033d6000805160206107c28339815191525490565b604080516001600160a01b03928316815291841660208301520160405180910390a161016a816000805160206107c283398151915255565b600061038d6000805160206107c28339815191525490565b6001600160a01b0316336001600160a01b0316141561025157506000805160206107c28339815191525490565b60606103df83836040518060600160405280602781526020016107e2602791396104f2565b9392505050565b3b151590565b6000805160206107c2833981519152546001600160a01b0316336001600160a01b031614156101365760405162461bcd60e51b815260206004820152604260248201527f5472616e73706172656e745570677261646561626c6550726f78793a2061646d60448201527f696e2063616e6e6f742066616c6c6261636b20746f2070726f78792074617267606482015261195d60f21b608482015260a4016102fd565b3660008037600080366000845af43d6000803e8080156104ad573d6000f35b3d6000fd5b6104bb816105c6565b6040516001600160a01b038216907fbc7cd75a20ee27fd9adebab32041f755214dbc6bffa90cc0225b39da2e5c2d3b90600090a250565b6060833b6105515760405162461bcd60e51b815260206004820152602660248201527f416464726573733a2064656c65676174652063616c6c20746f206e6f6e2d636f6044820152651b9d1c9858dd60d21b60648201526084016102fd565b600080856001600160a01b03168560405161056c9190610772565b600060405180830381855af49150503d80600081146105a7576040519150601f19603f3d011682016040523d82523d6000602084013e6105ac565b606091505b50915091506105bc828286610653565b9695505050505050565b803b61062f5760405162461bcd60e51b815260206004820152603260248201527f4552433139363750726f78793a206e657720696d706c656d656e746174696f6e604482015271081a5cc81b9bdd08184818dbdb9d1c9858dd60721b60648201526084016102fd565b7f360894a13ba1a3210667c828492db98dca3e2076cc3735a920a3ca505d382bbc55565b606083156106625750816103df565b8251156106725782518084602001fd5b8160405162461bcd60e51b81526004016102fd919061078e565b80356001600160a01b03811681146106a357600080fd5b919050565b6000602082840312156106ba57600080fd5b6103df8261068c565b6000806000604084860312156106d857600080fd5b6106e18461068c565b9250602084013567ffffffffffffffff808211156106fe57600080fd5b818601915086601f83011261071257600080fd5b81358181111561072157600080fd5b87602082850101111561073357600080fd5b6020830194508093505050509250925092565b60005b83811015610761578181015183820152602001610749565b838111156101e75750506000910152565b60008251610784818460208701610746565b9190910192915050565b60208152600082518060208401526107ad816040850160208701610746565b601f01601f1916919091016040019291505056feb53127684a568b3173ae13b9f8a6016e243e63b6e8ee1178d6a717850b5d6103416464726573733a206c6f772d6c6576656c2064656c65676174652063616c6c206661696c6564a2646970667358221220c6b4e2e3c61816b1ccf72791ec6b756e523161df5c3419f3fd4a4c350b0c3b7264736f6c63430008090033b53127684a568b3173ae13b9f8a6016e243e63b6e8ee1178d6a717850b5d6103360894a13ba1a3210667c828492db98dca3e2076cc3735a920a3ca505d382bbc416464726573733a206c6f772d6c6576656c2064656c65676174652063616c6c206661696c6564";

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
