package net.pixlies.pixliesfun.commands;

import co.aikar.commands.BaseCommand;
import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;

public class CommandManager {

    private final ImmutableList<BaseCommand> commands = ImmutableList.of(

    );

    public void registerAll() {
        commands.forEach(command -> Main.getInstance().getCommandManager().register(command, true));
    }

    public void unregisterAll() {
        commands.forEach(command -> Main.getInstance().getCommandManager().unregister(command));
    }

}
