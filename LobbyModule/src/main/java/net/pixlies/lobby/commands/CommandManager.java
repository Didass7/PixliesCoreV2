package net.pixlies.lobby.commands;

import co.aikar.commands.BaseCommand;
import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.lobby.commands.impl.LeaveQueueCommand;
import net.pixlies.lobby.commands.impl.LobbySettingsCommand;
import net.pixlies.lobby.commands.impl.PlayCommand;

public class CommandManager {

    private final ImmutableList<BaseCommand> commands = ImmutableList.of(
            new LobbySettingsCommand(),
            new PlayCommand(),
            new LeaveQueueCommand()
    );

    public void registerAll() {
        commands.forEach(command -> Main.getInstance().getCommandManager().register(command, false));
    }

}
