/*
 * Copyright 2021 ConsenSys Software Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package net.consensys.htlcbridge.transfer;

import java.math.BigInteger;

public enum TransferState {
  OPEN(0),
  REFUNDED(1),
  FINALILISED(2),
  TIMEDOUT(3),
  INVALID(4);

  int state;

  private TransferState(int state) {
    this.state = state;
  }

  public static TransferState create(BigInteger stateB) {
    long statel = stateB.longValue();
    switch ((int)statel) {
      case 0:
        return OPEN;
      case 1:
        return REFUNDED;
      case 2:
        return FINALILISED;
      case 3:
        return TIMEDOUT;
      default:
        return INVALID;
    }
  }

  public boolean equals(TransferState other) {
    return other.state == this.state;
  }

  public boolean equals(BigInteger other) {
    return other.longValue() == this.state;
  }
}
