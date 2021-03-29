# Bi-Directional HTLC based Token Bridge

The repo contains code for a bi-directional Hash Time-Locked Contracts (HTLC) 
based token bridge.  As shown in the simplified architecture diagram below, 
ERC20HtlcTransfer contracts are deployed on two blockchains. They allow ERC 20
tokens from one blockchain to be moved to another blockchain, and then later 
moved back to the original blockchain.

![BiDirectional Bridge](https://raw.githubusercontent.com/ConsenSys/htlc-bridge/main/docs/bidirectional-bridge.png)

## TODO
Need to write a section introducing users and relayers.



## Sequence
The diagram below walks through a standard sequence of steps required for a 
user to transfer some tokens from an ERC20 contract on one blockchain to
another blockchain.

![Sequence](https://raw.githubusercontent.com/ConsenSys/htlc-bridge/main/docs/htlc-transfer.png)

Walking through the diagram:
* A user submits a transaction to the ERC 20 contract on the source blockchain to 
 approve up to a certain number of tokens to be transferred by the 
 ```ERC20HtlcTransfer``` contract by calling the ERC 20 contract's 
 ```approve``` function. 
* The user generates a random pre-image-salt, and calculates a commitment based on 
the pre-image-salt, the user's address, the address of the ERC 20 contract on
the source blockchain, and the number of tokens to be transferred.
* The user submits a transaction to the ```ERC20HtlcTransfer```, 
```newTransferToOtherBlockchain```,
specifying the token address, the number of tokens to transfer, and the commitment
value.
* During the ```newTransferToOtherBlockchain``` call, 
the ```ERC20HtlcTransfer``` contract 
calls the ERC 20 contract's ```transferFrom``` function to transfer tokens from the user
to the ```ERC20HtlcTransfer``` contract. Additionally, a ```SourceTransferInit``` event
is emitted.
* Relayers monitor the source blockchain, looking for ```SourceTransferInit``` events.
* When a relayer sees a ```SourceTransferInit``` event, it submits a transaction to the
```ERC20HtlcTransfer``` contract on the destination blockchain, calling 
the ```newTransferFromOtherBlockchain``` function, submitting the 
user's account address, the ERC 20 token's address on the source blockchain, 
and the amount to transfer. A ```DestTransferInit``` event is emitted as part of the execution of the 
```newTransferFromOtherBlockchain``` function.
* The user monitors the destination blockchain, waiting for the ```DestTransferInit``` 
event to be emitted. 
* When the user sees the ```DestTransferInit``` event for their transfer, they call
the ```finaliseTransferFromOtherBlockchain``` function on the ```ERC20HtlcTransfer```
contract on the destination blockchain, passing in the commitment and 
the pre-image-salt, to finalise the transfer from their perspective.
* When the ```finaliseTransferFromOtherBlockchain``` function is called, the code
looks up the mapping between the source blockchain's ERC contract address to 
destination blockchain's ERC 20 contract address, and then calls the ```transfer```
function on the ERC20 contract on the destination blockchain to transfer
the tokens from the ```ERC20HtlcTransfer``` contract to the user. The 
```DestTransferCompleted``` event is emitted.
* Relayers monitor the destination blockchain, looking for ```DestTransferCompleted```
events.  
* When a relayer sees a ```DestTransferCompleted``` event, it submits a transaction
to call the ```finaliseTransferToOtherBlockchain``` on the ```ERC20HtlcTransfer``` contract 
on the source blockchain, submitting the commitment and the pre-image-salt. 
Doing this finalises the transfer from the relayer's perspective.

## Time-outs and Refunds
The ```ERC20HtlcTransfer``` contracts have source and destination 
time-lock periods. These time-locks help ensure the funds are transferred 
securely. The source time-lock starts when the user calls the 
```newTransferToOtherBlockchain``` function on the ```ERC20HtlcTransfer```
contract on the source blockchain. The destination time-lock starts when
a Relayer submits the ```newTransferFromOtherBlockchain``` function on the   
```ERC20HtlcTransfer``` contract on the destination blockchain.

The transfer times-out on the destination blockchain if the
User has not called the ```finaliseTransferFromOtherBlockchain``` function 
on the ```ERC20HtlcTransfer``` contract on the destination blockchain prior
to the destination time-lock period. Calling this function makes the  
pre-image-salt value to the Relayers.

In some circumstances, the User may need to execute a refund. If the 
transfer were to fail, for instance if no Relayer called
```newTransferFromOtherBlockchain``` function on the
```ERC20HtlcTransfer``` contract on the destination blockchain, or 
if the User did not call the 
```finaliseTransferFromOtherBlockchain``` function 
on the ```ERC20HtlcTransfer``` contract on the destination blockchain,
then the User's tokens on the source blockchain would be owned by
the ```ERC20HtlcTransfer``` and they wouldn't have any tokens on the 
destination blockchain. In this case, the User is able to request a 
refund by calling the ```refundTransferToOtherBlockchain``` function 
on the ```ERC20HtlcTransfer``` contract on the source blockchain. This 
function must be called after the source time-lock period has expired.
 
It should be noted that if the Relayers fail to call 
```finaliseTransferToOtherBlockchain``` on the ```ERC20HtlcTransfer``` contract 
on the source blockchain prior the source time-lock period expiring
after the User has called the ```finaliseTransferFromOtherBlockchain``` function 
on the ```ERC20HtlcTransfer``` contract on the destination blockchain, then
the User will have the tokens on the destination blockchain and be able to 
request a refund on the source blockchain. to prevent this from occurring 
**the destination time-lock period should be a significantly
shorter period of time than the source time-lock period to protect the relayer
from malicious users**. 


## Future Work:

* More unit tests and integration tests.
* More documentation.
* Improve performance of relayer.
* Support for ERC 721 NFT tokens are coming!