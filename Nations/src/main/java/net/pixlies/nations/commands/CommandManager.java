package net.pixlies.nations.commands;

import co.aikar.commands.BaseCommand;
import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.nations.commands.impl.NationCommand;

public class CommandManager {

    private final ImmutableList<BaseCommand> commands = ImmutableList.of(
            // NATIONS
            new NationCommand()
    );

    public void registerAllCommands() {
        commands.forEach(command -> Main.getInstance().getCommandManager().register(command, true));
    }

    public void unregisterAllCommands() {
        commands.forEach(command -> Main.getInstance().getCommandManager().unregister(command));
    }

}
