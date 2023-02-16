package net.pixlies.business.commands;

import co.aikar.commands.BaseCommand;
import com.google.common.collect.ImmutableList;
import net.pixlies.business.commands.economy.*;
import net.pixlies.business.commands.market.MarketCommand;
import net.pixlies.business.commands.market.MarketStatsCommand;
import net.pixlies.business.commands.market.PriceCommand;
import net.pixlies.business.commands.market.TradeBlockCommand;
import net.pixlies.business.commands.nations.EmbargoCommand;
import net.pixlies.business.commands.nations.TariffCommand;
import net.pixlies.business.commands.nations.TaxCommand;
import net.pixlies.core.Main;

public class CommandManager {
      private final ImmutableList<BaseCommand> commands = ImmutableList.of(
              new MarketCommand(),
              new PriceCommand(),
              new TariffCommand(),
              new TradeBlockCommand(),
              new MarketStatsCommand(),
              new BalanceCommand(),
              new BalTopCommand(),
              new EconomyCommand(),
              new PayCommand(),
              new GoldStandardCommand(),
              new ConvertCoinsCommand(),
              new ConvertGoldCommand(),
              new EmbargoCommand(),
              new TaxCommand()
      );
      
      public void registerAllCommands() {
            commands.forEach(command -> Main.getInstance().getCommandManager().register(command, true));
      }
      
      public void unregisterAllCommands() {
            commands.forEach(command -> Main.getInstance().getCommandManager().unregister(command));
      }
}
