package net.consensys.htlcbridge.common;

import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.AbiTypes;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;

import java.util.Collections;
import java.util.List;

public class RevertReason {


  public static String decodeRevertReason(String revertReasonEncoded) {
    String errorMethodId = "0x08c379a0"; // Numeric.toHexString(Hash.sha3("Error(string)".getBytes())).substring(0, 10)
    List<TypeReference<Type>> revertReasonTypes = Collections.singletonList(TypeReference.create((Class<Type>) AbiTypes.getType("string")));

    if (revertReasonEncoded == null) {
      return "Revert Reason is null";
    }

    if (revertReasonEncoded.startsWith(errorMethodId)) {
      String encodedRevertReason = revertReasonEncoded.substring(errorMethodId.length());
      List<Type> decoded = FunctionReturnDecoder.decode(encodedRevertReason, revertReasonTypes);
      Utf8String decodedRevertReason = (Utf8String) decoded.get(0);
      return decodedRevertReason.getValue();
    }
    return revertReasonEncoded;
  }
}

