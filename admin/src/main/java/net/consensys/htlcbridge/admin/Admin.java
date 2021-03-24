package net.consensys.htlcbridge.admin;

import net.consensys.htlcbridge.admin.commands.DeployERC20Contract;
import net.consensys.htlcbridge.admin.commands.DeployReceiverContract;
import net.consensys.htlcbridge.admin.commands.DeployTransferContract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Admin {
  private static final Logger LOG = LogManager.getLogger(Admin.class);

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      showHelp();
      return;
    }

    String cmd = args[0];

    if (cmd.equalsIgnoreCase("deptx")) {
      DeployTransferContract.deploy(args);
    }
    else if (cmd.equalsIgnoreCase("deprx")) {
      DeployReceiverContract.deploy(args);
    }
    else if (cmd.equalsIgnoreCase("deperc20")) {
      DeployERC20Contract.deploy(args);
    }
    else {
      LOG.info("Unknown command");
      showHelp();
    }

  }


  public static void showHelp() {
    LOG.info("Bridge Admin command line options:");
    LOG.info("deptx: Deploy Tranfer contract");
  }

}