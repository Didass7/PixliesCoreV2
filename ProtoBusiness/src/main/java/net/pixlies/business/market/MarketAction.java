package net.pixlies.business.market;

import net.pixlies.business.ProtoBusinesss;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public enum MarketAction {
      CREATE_ORDER,
      DELETE_ORDER,
      BUY,
      SELL;
      
      private static final ProtoBusinesss instance = ProtoBusinesss.getInstance();
      
      public String format(String... args) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String string = dtf.format(now) + " " + args[0] + " ";
            switch (this) {
                  case CREATE_ORDER -> {
                        return string + "created " + args[1] + " order \"" + args[2] + "\" on " + args[3] + "x " +
                                args[4] + " for " + args[5] + " coins each";
                  }
                  case DELETE_ORDER -> {
                        return string + "deleted " + args[1] + " order \"" + args[2] + "\"";
                  }
                  case BUY -> {
                        return string + "(\"" + args[1] + "\") bought " + args[2] + "x " + args[3] + " for " +
                                args[4] + " coins each from order \"" + args[5] + "\"";
                  }
                  case SELL -> {
                        return string + "(\"" + args[1] + "\") sold " + args[2] + "x " + args[3] + " for " +
                                args[4] + " coins each from order \"" + args[5] + "\"";
                  }
            }
            return string + "INVALID";
      }
      
      public static void updateLog(String action) {
            List<String> log = instance.getLog().getStringList("log");
            log.add(action);
            instance.getLog().set("log", log);
            instance.getLog().save();
      }
      
      /* timestamp = prefix
      <playerName> created <type> order "<orderId>" on <amount>x <itemName> for <price> coins each
      <playerName> deleted <type> order "<orderId>"
      <playerName> ("<buyOrderId>") bought <amount>x <itemName> for <price> coins each from order "<sellOrderId>"
      <playerName> ("<sellOrderId>") sold <amount>x <itemName> for <price> coins each to order "<buyOrderId>"
       */
}
