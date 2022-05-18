package net.pixlies.business.commands;

import co.aikar.commands.BaseCommand;
import com.google.common.collect.ImmutableList;
import net.pixlies.business.commands.impl.CompanyCommand;
import net.pixlies.business.commands.impl.MarketCommand;
import net.pixlies.business.commands.impl.PriceCommand;
import net.pixlies.core.Main;

public class CommandManager {

    private final ImmutableList<BaseCommand> commands = ImmutableList.of(
            // COMPANIES
            new CompanyCommand(),
            new MarketCommand(),
            new PriceCommand()
    );

    public void registerAllCommands() {
        commands.forEach(command -> Main.getInstance().getCommandManager().register(command, true));
    }

    public void unregisterAllCommands() {
        commands.forEach(command -> Main.getInstance().getCommandManager().unregister(command));
    }

}
