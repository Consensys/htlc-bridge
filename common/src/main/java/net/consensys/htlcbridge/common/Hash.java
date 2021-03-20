package net.consensys.htlcbridge.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public abstract class Hash {
  public static final String KECCAK256_ALG = "KECCAK-256";
  private static final String SHA256_ALG = "SHA-256";

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  private Hash() {
  }

  /**
   * Digest using SHA2-256.
   *
   * @param input The input bytes to produce the digest for.
   * @return A digest.
   */
  public static Bytes32 sha256(final Bytes input) {
    return Bytes32.wrap(digestUsingAlgorithm(input, SHA256_ALG));
  }

  /**
   * Digest using keccak-256.
   *
   * @param input The input bytes to produce the digest for.
   * @return A digest.
   */
  public static Bytes32 keccak256(final Bytes input) {
    return Bytes32.wrap(digestUsingAlgorithm(input, KECCAK256_ALG));
  }


  /**
   * Helper method to generate a digest using the provided algorithm.
   *
   * @param input The input bytes to produce the digest for.
   * @param alg   The name of the digest algorithm to use.
   * @return A digest.
   */
  private static byte[] digestUsingAlgorithm(final Bytes input, final String alg) {
    try {
      final MessageDigest digest = MessageDigest.getInstance(alg);
      input.update(digest);
      return digest.digest();
    } catch (final NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}