package net.pixlies.core.commands;

import co.aikar.commands.*;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.commands.admin.ModulesCommand;
import net.pixlies.core.commands.cosmetics.*;
import net.pixlies.core.commands.moderation.*;
import net.pixlies.core.commands.player.*;
import net.pixlies.core.commands.staff.*;
import net.pixlies.core.entity.User;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PixliesCommandManager {

    private static final Main instance = Main.getInstance();

    private final List<BaseCommand> registeredCommands = new ArrayList<>();

    private @Getter final PaperCommandManager pcm;
    private final boolean limitedCommands = instance.getConfig().getBoolean("commands.limitedCommands", false);

    public PixliesCommandManager() {

        pcm = new PaperCommandManager(instance);

        pcm.enableUnstableAPI("help");
        pcm.enableUnstableAPI("brigadier");

        registerContexts();
        registerConditions();
        registerAllCommands();

    }

    private void registerAllCommands() {

        // MODERATION
        register(new BanCommand(), false);
        register(new TempBanCommand(), false);
        register(new UnBanCommand(), false);
        register(new MuteCommand(), false);
        register(new TempMuteCommand(), false);
        register(new UnMuteCommand(), false);
        register(new ChatCommand(), false);
        register(new InventorySeeCommand(), false);
        register(new GlobalPvpCommand(), false);
        register(new KickallCommand(), false);
        register(new VanishCommand(), false);
        register(new TpHereCommand(), false);
        register(new TpAskCommand(), false);
        register(new TpAllCommand(), false);
        register(new TpAcceptCommand(), false);
        register(new TpBackCommand(), false);
        register(new BlacklistCommand(), false);
        register(new UnBlacklistCommand(), false);
        register(new FreezeCommand(), false);

        // STAFF
        register(new StaffModeCommand(), true);
        register(new GodCommand(), false);
        register(new WorldCommand(), false);
        register(new GamemodeCommand(), false);
        register(new StaffSettingsCommand(), false);
        register(new SudoCommand(), false);
        register(new BreakCommand(), false);
        register(new ExtinguishCommand(), false);
        register(new BroadcastCommand(), false);
        register(new TpCommand(), false);
        register(new ModelDataCommand(), false);
        register(new SpeedCommand(), false);

        // DEBUG
        register(new ModulesCommand(), false);

        // COSMETICS
        register(new HealCommand(), false);
        register(new FeedCommand(), false);
        register(new SitCommand(), false);
        register(new NickNameCommand(), false);
        register(new PollCommand(), false);
        register(new EnderChestCommand(), true);
        register(new NightVisionCommand(), true);
        register(new RepairCommand(), true);
        register(new AnvilCommand(), true);
        register(new WorkBenchCommand(), true);
        register(new SmithingTableCommand(), true);
        register(new StoneCutterCommand(), true);
        register(new CartographyTableCommand(), true);
        register(new GrindStoneCommand(), true);
        register(new RenameCommand(), true);
        register(new HatCommand(), true);
        register(new LoomCommand(), true);

        // PLAYER
        register(new MessageCommand(), false);
        register(new ReplyCommand(), false);
        register(new TprCommand(), false);
        register(new ClearCommand(), false);
        register(new EchoCommand(), false);
        register(new DiscordCommand(), false);
        register(new RulesCommand(), false);
        register(new PlaytimeCommand(), false);
        register(new AfkCommand(), true);
        register(new SuicideCommand(), true);

    }

    private void registerContexts() {
            CommandContexts<BukkitCommandExecutionContext> contexts = pcm.getCommandContexts();
            contexts.registerContext(User.class, context ->
                    User.get(context.getPlayer().getUniqueId()));
    }

    private void registerConditions() {
        pcm.getCommandConditions().addCondition(Integer.class, "limits", (context, execution, value) -> {
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
    public void register(@NotNull BaseCommand command, boolean notLobby) {
        if (notLobby && limitedCommands) return;
        pcm.registerCommand(command);
        registeredCommands.add(command);
    }

    public void unregister(@NotNull BaseCommand command) {
        if (!(registeredCommands.contains(command)))
            return;
        pcm.unregisterCommand(command);
        registeredCommands.remove(command);
    }

}
