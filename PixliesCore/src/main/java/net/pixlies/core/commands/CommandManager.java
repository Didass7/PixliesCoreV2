package net.pixlies.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.RootCommand;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.commands.cosmetics.*;
import net.pixlies.core.commands.admin.*;
import net.pixlies.core.commands.moderation.*;
import net.pixlies.core.commands.player.ClearCommand;
import net.pixlies.core.commands.player.MessageCommand;
import net.pixlies.core.commands.player.ReplyCommand;
import net.pixlies.core.commands.player.SuicideCommand;
import net.pixlies.core.commands.staff.*;

public class CommandManager {

    private static final Main instance = Main.getInstance();

    private @Getter final PaperCommandManager pcm;
    private final boolean limitedCommands = instance.getConfig().getBoolean("commands.limitedCommands", false);

    public CommandManager() {
        pcm = new PaperCommandManager(instance);

        pcm.enableUnstableAPI("help");
        pcm.enableUnstableAPI("brigadier");

        registerConditions();
        registerAllCommands();
    }

    private void registerAllCommands() {

        // MODERATION
        register(new BanCommand(), false);
        register(new TempBanCommand(), false);
        register(new MuteCommand(), false);
        register(new TempMuteCommand(), false);
        register(new ChatCommand(), false);
        register(new InventorySeeCommand(), false);
        register(new ToggleGlobalPvpCommand(), false);
        register(new KickallCommand(), false);
        register(new StaffModeCommand(), true);

        // STAFF
        register(new GodCommand(), false);
        register(new StaffModeSettingsCommand(), false);
        register(new WorldCommand(), false);
        register(new GamemodeCommand(), false);

        // DEBUG
        register(new ModulesCommand(), false);

        // COSMETICS
        register(new HealCommand(), false);
        register(new FeedCommand(), false);
        register(new EnderChestCommand(), true);
        register(new NightVisionCommand(), true);
        register(new RepairCommand(), true);
        register(new SuicideCommand(), true);

        // PLAYER
        register(new MessageCommand(), false);
        register(new ReplyCommand(), false);
        register(new RandomTeleportCommand(), false);
        register(new ClearCommand(), false);

    }

    private void registerConditions() {
        pcm.getCommandConditions().addCondition(Integer.class, "intLimits", (context, execution, value) -> {
            if (value == null) {
                return;
            }
            if (context.hasConfig("min") && context.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("Min value must be " + context.getConfigValue("min", 0));
            }
            if (context.hasConfig("max") && context.getConfigValue("max", 3) < value) {
                throw new ConditionFailedException("Max value must be " + context.getConfigValue("max", 3));
            }
        });
        pcm.getCommandConditions().addCondition(Long.class, "longLimits", (context, execution, value) -> {
            if (value == null) {
                return;
            }
            if (context.hasConfig("min") && context.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("Min value must be " + context.getConfigValue("min", 0));
            }
            if (context.hasConfig("max") && context.getConfigValue("max", 3) < value) {
                throw new ConditionFailedException("Max value must be " + context.getConfigValue("max", 3));
            }
        });
    }

    /**
     * Registers a command
     * @param command the command to register
     * @param notLobby if the command can be used on a server that isn't lobby
     */
    public void register(BaseCommand command, boolean notLobby) {
        if (notLobby && limitedCommands) return;
        pcm.registerCommand(command);
    }

    public void unregister(BaseCommand command) {
        if (!pcm.getRegisteredRootCommands().contains((RootCommand) command)) return;
        pcm.unregisterCommand(command);
    }

}
