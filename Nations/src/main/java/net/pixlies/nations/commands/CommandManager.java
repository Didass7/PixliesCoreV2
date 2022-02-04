package net.pixlies.nations.commands;

import co.aikar.commands.BaseCommand;
import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.nations.commands.impl.NationCommand;
import net.pixlies.nations.commands.impl.player.LockCommand;

public class CommandManager {

    private final ImmutableList<BaseCommand> commands = ImmutableList.of(
            // NATIONS
            new NationCommand(),

            // PLAYER
            new LockCommand()

    );

    public void registerAllCommands() {
        Main.getInstance().getCommandManager().getPcm().getCommandContexts().registerContext(User.class, c -> User.get(c.getPlayer().getUniqueId()));
        commands.forEach(command -> Main.getInstance().getCommandManager().register(command, true));
    }

    public void unregisterAllCommands() {
        commands.forEach(command -> Main.getInstance().getCommandManager().unregister(command));
    }

}
