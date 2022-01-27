package net.pixlies.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.commands.debug.ModulesCommand;
import net.pixlies.core.commands.moderation.*;

public class CommandManager {

    private static final Main instance = Main.getInstance();

    private @Getter final PaperCommandManager pcm;

    public CommandManager() {
        pcm = new PaperCommandManager(instance);

        pcm.enableUnstableAPI("help");
        pcm.enableUnstableAPI("brigadier");

        registerAllCommands();
    }

    public void registerAllCommands() {
        // MODERATION
        register(new BanCommand());
        register(new TempBanCommand());
        register(new MuteCommand());
        register(new TempMuteCommand());
        register(new ChatCommand());
        register(new GodCommand());
        register(new StaffSettingsCommand());

        // DEBUG
        register(new ModulesCommand());
    }

    public void register(BaseCommand command) {
        pcm.registerCommand(command);
    }

    public void unregister(BaseCommand command) {
        pcm.unregisterCommand(command);
    }

}
