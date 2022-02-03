package net.pixlies.lobby.commands;

import co.aikar.commands.BaseCommand;
import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.lobby.commands.impl.LobbySettingsCommand;

public class CommandManager {

    private final ImmutableList<BaseCommand> commands = ImmutableList.of(
            new LobbySettingsCommand()
    );

    public void registerAll() {
        commands.forEach(command -> Main.getInstance().getCommandManager().register(command, false));
    }

}
