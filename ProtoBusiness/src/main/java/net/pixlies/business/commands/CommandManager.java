package net.pixlies.business.commands;

import co.aikar.commands.BaseCommand;
import com.google.common.collect.ImmutableList;
import net.pixlies.business.commands.economy.BalTopCommand;
import net.pixlies.business.commands.economy.BalanceCommand;
import net.pixlies.business.commands.economy.EconomyCommand;
import net.pixlies.business.commands.economy.PayCommand;
import net.pixlies.business.commands.market.*;
import net.pixlies.core.Main;

public class CommandManager {
      private final ImmutableList<BaseCommand> commands = ImmutableList.of(
              new MarketCommand(),
              new PriceCommand(),
              new TariffCommand(),
              new ChallengeCommand(),
              new TradeBlockCommand(),
              new MarketStatsCommand(),
              new BalanceCommand(),
              new BalTopCommand(),
              new EconomyCommand(),
              new PayCommand()
      );
      
      public void registerAllCommands() {
            commands.forEach(command -> Main.getInstance().getCommandManager().register(command, true));
      }
      
      public void unregisterAllCommands() {
            commands.forEach(command -> Main.getInstance().getCommandManager().unregister(command));
      }
}
