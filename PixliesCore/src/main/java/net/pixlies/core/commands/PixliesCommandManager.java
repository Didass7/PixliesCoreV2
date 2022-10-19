package net.pixlies.core.commands;

import co.aikar.commands.*;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.commands.cosmetics.*;
import net.pixlies.core.commands.moderation.*;
import net.pixlies.core.commands.player.*;
import net.pixlies.core.commands.staff.*;
import net.pixlies.core.entity.Warp;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PixliesCommandManager {

    private static final Main instance = Main.getInstance();

    private final List<BaseCommand> registeredCommands = new ArrayList<>();

    private @Getter final PaperCommandManager pcm;
    private final boolean limitedCommands = instance.isLobbyServer();

    public PixliesCommandManager() {

        pcm = new PaperCommandManager(instance);

        pcm.enableUnstableAPI("help");
        pcm.enableUnstableAPI("brigadier");

        registerContexts();
        registerLocaleSettings();
        registerConditions();
        registerAllCommands();

    }

    private void registerAllCommands() {
        // MODERATION
        register(new BanCommand(), false);
        register(new TempBanCommand(), false);
        register(new UnBanCommand(), false);
        register(new BanSpyCommand(), false);
        register(new MuteCommand(), false);
        register(new TempMuteCommand(), false);
        register(new UnMuteCommand(), false);
        register(new ChatCommand(), false);
        register(new InventorySeeCommand(), false);
        register(new KickallCommand(), false);
        register(new VanishCommand(), false);
        register(new JoinVanishedCommand(), false);
        register(new TpHereCommand(), false);
        register(new TpAskCommand(), false);
        register(new TpAcceptCommand(), false);
        register(new TpBackCommand(), false);
        register(new BlacklistCommand(), false);
        register(new UnBlacklistCommand(), false);
        register(new FreezeCommand(), false);
        register(new KickCommand(), false);
        register(new MuteSpyCommand(), false);
        register(new SocialSpyCommand(), false);
        register(new CommandSpyCommand(), false);

        // STAFF
        register(new StaffModeCommand(), false);
        register(new WorldCommand(), false);
        register(new GamemodeCommand(), false);
        register(new SudoCommand(), false);
        register(new BreakCommand(), false);
        register(new ExtinguishCommand(), false);
        register(new BroadcastCommand(), false);
        register(new TpCommand(), false);
        register(new ModelDataCommand(), false);
        register(new SpeedCommand(), false);
        register(new BypassCommand(), false);
        register(new StaffChatCommand(), false);
        register(new TimeCommand(), false);
        register(new SetSpawnCommand(), false);
        register(new FlyCommand(), false);

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
        register(new SpawnCommand(), false);
        register(new WarpCommand(), false);
        register(new AfkCommand(), true);
        register(new SuicideCommand(), true);
    }

    private void registerContexts() {
            CommandContexts<BukkitCommandExecutionContext> contexts = pcm.getCommandContexts();

            contexts.registerContext(User.class, context ->
                    User.get(context.getPlayer().getUniqueId()));

            contexts.registerContext(Location.class, context -> {
                String first = context.popFirstArg();
                String second = context.popFirstArg();
                String third = context.popFirstArg();
                int x;
                int y;
                int z;
                try {
                    x = Integer.parseInt(first);
                    y = Integer.parseInt(second);
                    z = Integer.parseInt(third);
                } catch (NumberFormatException e) {
                    throw new ConditionFailedException("That isn't a valid location.");
                }
                Location location;
                if (context.getSender() instanceof Player player) {
                    location = new Location(player.getWorld(), x, y, z);
                } else {
                    World world = Bukkit.getWorld("world");
                    if (world == null) {
                        throw new ConditionFailedException("You can't execute this command as console.");
                    }
                    location = new Location(world, x, y, z);
                }
                return location;
            });

            contexts.registerContext(Warp.class, context -> {
                String name = context.getFirstArg();
                Warp warp = Warp.get(name);

                if (warp == null) {
                    if (context.isOptional()) {
                        return null;
                    }
                    throw new ConditionFailedException(Lang.PIXLIES + "§7That isn't a valid warp.");
                }

                return warp;

            });

    }

    private void registerLocaleSettings() {
        pcm.usePerIssuerLocale(false);

        pcm.setFormat(MessageType.ERROR, ChatColor.RED, ChatColor.WHITE);
        pcm.setFormat(MessageType.HELP, ChatColor.AQUA, ChatColor.WHITE, ChatColor.DARK_GRAY, ChatColor.GRAY);
        pcm.setFormat(MessageType.SYNTAX, 1, ChatColor.RED);
        pcm.setFormat(MessageType.INFO, ChatColor.RED, ChatColor.WHITE);
    }

    private void registerConditions() {
        pcm.getCommandConditions().addCondition(Integer.class, "limits", (context, execution, value) -> {
            if (value == null) {
                return;
            }
            if (context.hasConfig("min") && context.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException(Lang.PIXLIES + CC.format("&7You can only enter a minimum value of &6" + context.getConfigValue("min", 0) + "&7."));
            }
            if (context.hasConfig("max") && context.getConfigValue("max", 3) < value) {
                throw new ConditionFailedException(Lang.PIXLIES + CC.format("&7You can only enter a maximum value of &6" + context.getConfigValue("max", 3) + "&7."));
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
