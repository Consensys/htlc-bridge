/*
 * Copyright 2019 ConsenSys Software Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.consensys.htlcbridge.common;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.apache.tuweni.bytes.Bytes32;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.security.DrbgParameters;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Enumeration;

import static java.security.DrbgParameters.Capability.RESEED_ONLY;

public class PRNG {
  static SecureRandom publicPRNG = createPRNG();
  static SecureRandom privatePRNG = createPRNG();

  public static Bytes32 getPublicRandomBytes32() {
    return Bytes32.random(publicPRNG);
  }

  public static void reseedKick() {
    publicPRNG.setSeed(System.nanoTime());
  }


  public static SecureRandom createPRNG() {
    try {
      return SecureRandom.getInstance("DRBG",
          DrbgParameters.instantiation(256, RESEED_ONLY, getPersonalizationString()));
    } catch (Exception ex) {
      throw new Error(ex);
    }
  }


  // Use a personalisation string to help ensure the entropy going into the PRNG is unique.
  private static byte[] getPersonalizationString() throws SocketException, BufferOverflowException {
    final byte[] now = Longs.toByteArray(System.nanoTime());
    final byte[] networkMacs = networkHardwareAddresses();
    final Runtime runtime = Runtime.getRuntime();
    final byte[] threadId = Longs.toByteArray(Thread.currentThread().getId());
    final byte[] availProcessors = Ints.toByteArray(runtime.availableProcessors());
    final byte[] freeMem = Longs.toByteArray(runtime.freeMemory());
    final byte[] runtimeMem = Longs.toByteArray(runtime.maxMemory());
    return Bytes.concat(now, threadId, availProcessors, freeMem, runtimeMem, networkMacs);
  }

  private static byte[] networkHardwareAddresses() throws SocketException, BufferOverflowException {
    final byte[] networkAddresses = new byte[256];
    final ByteBuffer buffer = ByteBuffer.wrap(networkAddresses);

    final Enumeration<NetworkInterface> networkInterfaces =
        NetworkInterface.getNetworkInterfaces();
    if (networkInterfaces != null) {
      while (networkInterfaces.hasMoreElements()) {
        final NetworkInterface networkInterface = networkInterfaces.nextElement();
        final byte[] hardwareAddress = networkInterface.getHardwareAddress();
        if (hardwareAddress != null) {
          buffer.put(hardwareAddress);
        }
      }
    }
    return Arrays.copyOf(networkAddresses, buffer.position());
  }
}
