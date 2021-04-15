#!/bin/bash
set -e

docker run -p 8545:8545 hyperledger/besu:latest --miner-enabled --miner-coinbase fe3b557e8fb62b89f4916b721be55ceb828dbd73 --rpc-http-enabled --network-id=31 --min-gas-price=0

