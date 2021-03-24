package net.consensys.htlcbridge.relayer.data;



import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

  Map<byte[], TransferInformation> allTransfers = new ConcurrentHashMap<>();

  private static DataStore instance = new DataStore();

  public static DataStore getInstance() {
    return instance;
  }

  public void addOrReplace(TransferInformation info) {
    allTransfers.put(info.getCommitment(), info);
  }

  public TransferInformation get(byte[] commitment) {
    return allTransfers.get(commitment);
  }

}
