package net.pixlies.proxy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BungeeCommandManager;
import com.google.common.collect.ImmutableList;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.commands.impl.player.LobbyCommand;
import net.pixlies.proxy.commands.impl.staff.MaintenanceCommand;

public class CommandManager {

    private static final Proxy instance = Proxy.getInstance();
    private final BungeeCommandManager manager = new BungeeCommandManager(instance);

    private final ImmutableList<BaseCommand> commands = ImmutableList.of(

            // PLAYER
            new LobbyCommand(),

            // STAFF
            new MaintenanceCommand()

    );

    public void registerCommands() {
        commands.forEach(manager::registerCommand);
    }

    public void registerCompletions() {
        // TODO
    }

}
