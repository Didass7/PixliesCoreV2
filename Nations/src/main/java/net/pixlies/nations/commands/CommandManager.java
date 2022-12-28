package net.pixlies.nations.commands;

import co.aikar.commands.BaseCommand;
import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.nations.commands.impl.NationCommand;
import net.pixlies.nations.commands.impl.nations.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommandManager {

    public static final Map<UUID, String> autoClaimPlayers = new HashMap<>();

    private final ImmutableList<BaseCommand> commands = ImmutableList.of(
            // NATIONS
            new NationCommand(),
            new NationClaimCommand(),
            new NationCreateCommand(),
            new NationDescriptionCommand(),
            new NationDisbandCommand(),
            new NationRenameCommand(),
            new NationMapCommand()
    );

    public void registerAllCommands() {
        Main.getInstance().getCommandManager().getPcm().getCommandReplacements().addReplacement("nationscommand", "nation|nations|n|faction|factions|f|country|countries");
        commands.forEach(command -> Main.getInstance().getCommandManager().register(command, true));
    }

    public void unregisterAllCommands() {
        commands.forEach(command -> Main.getInstance().getCommandManager().unregister(command));
    }

}
