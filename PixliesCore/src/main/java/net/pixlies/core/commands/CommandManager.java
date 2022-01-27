package net.pixlies.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.commands.cosmetics.EnderChestCommand;
import net.pixlies.core.commands.cosmetics.FeedCommand;
import net.pixlies.core.commands.cosmetics.HealCommand;
import net.pixlies.core.commands.cosmetics.NightVisionCommand;
import net.pixlies.core.commands.debug.ModulesCommand;
import net.pixlies.core.commands.moderation.*;
import net.pixlies.core.commands.staff.GodCommand;
import net.pixlies.core.commands.staff.StaffSettingsCommand;
import net.pixlies.core.commands.staff.WorldCommand;

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
        register(new InventorySeeCommand());

        // STAFF
        register(new GodCommand());
        register(new StaffSettingsCommand());
        register(new WorldCommand());

        // DEBUG
        register(new ModulesCommand());

        // COSMETICS
        register(new HealCommand());
        register(new EnderChestCommand());
        register(new FeedCommand());
        register(new NightVisionCommand());

    }

    public void register(BaseCommand command) {
        pcm.registerCommand(command);
    }

    public void unregister(BaseCommand command) {
        pcm.unregisterCommand(command);
    }

}
