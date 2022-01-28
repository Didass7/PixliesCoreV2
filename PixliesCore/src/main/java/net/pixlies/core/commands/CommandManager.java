package net.pixlies.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.RootCommand;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.commands.cosmetics.*;
import net.pixlies.core.commands.debug.*;
import net.pixlies.core.commands.moderation.*;
import net.pixlies.core.commands.staff.*;

public class CommandManager {

    private static final Main instance = Main.getInstance();

    private @Getter final PaperCommandManager pcm;
    private final boolean limitedCommands = instance.getConfig().getBoolean("commands.limitedCommands", false);

    public CommandManager() {
        pcm = new PaperCommandManager(instance);

        pcm.enableUnstableAPI("help");
        pcm.enableUnstableAPI("brigadier");

        registerAllCommands();
    }

    public void registerAllCommands() {

        // MODERATION
        register(new BanCommand(), false);
        register(new TempBanCommand(), false);
        register(new MuteCommand(), false);
        register(new TempMuteCommand(), false);
        register(new ChatCommand(), false);
        register(new InventorySeeCommand(), false);

        // STAFF
        register(new GodCommand(), false);
        register(new StaffSettingsCommand(), false);
        register(new WorldCommand(), false);

        // DEBUG
        register(new ModulesCommand(), false);

        // COSMETICS
        register(new HealCommand(), false);
        register(new FeedCommand(), false);
        register(new EnderChestCommand(), true);
        register(new NightVisionCommand(), true);
        register(new RepairCommand(), true);
        register(new SuicideCommand(), true);

    }

    /**
     * Registers a command
     * @param command the command to register
     * @param earthOnly if the command can be used on a server that isn't lobby
     */
    public void register(BaseCommand command, boolean earthOnly) {
        if (earthOnly && limitedCommands) return;
        pcm.registerCommand(command);
    }

    public void unregister(BaseCommand command) {
        if (!pcm.getRegisteredRootCommands().contains((RootCommand) command)) return;
        pcm.unregisterCommand(command);
    }

}
