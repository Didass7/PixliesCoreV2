package net.pixlies.nations.commands;

import co.aikar.commands.BaseCommand;
import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.nations.commands.impl.NationCommand;
import net.pixlies.nations.commands.impl.nations.*;

public class CommandManager {

    private final ImmutableList<BaseCommand> commands = ImmutableList.of(
            // NATIONS
            new NationCommand(),
            new NationClaimCommand(),
            new NationCreateCommand(),
            new NationDescriptionCommand(),
            new NationDisbandCommand(),
            new NationRenameCommand()
    );

    public void registerAllCommands() {
        Main.getInstance().getCommandManager().getPcm().getCommandReplacements().addReplacement("nationscommand", "nation|nations|n|faction|factions|f|country|countries");
        commands.forEach(command -> Main.getInstance().getCommandManager().register(command, true));
    }

    public void unregisterAllCommands() {
        commands.forEach(command -> Main.getInstance().getCommandManager().unregister(command));
    }

}
