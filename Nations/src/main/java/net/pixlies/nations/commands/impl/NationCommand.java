package net.pixlies.nations.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.nations.Nations;
import net.pixlies.nations.handlers.impl.NationDisbandHandler;
import net.pixlies.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.ranks.NationPermission;
import net.pixlies.nations.nations.ranks.NationRank;
import net.pixlies.nations.utils.NationUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("nation|nations|n|faction|factions|country|countries")
public class NationCommand extends BaseCommand {

    private static final Nations instance = Nations.getInstance();
    private final NationDisbandHandler disbandHandler = instance.getHandlerManager().getHandler(NationDisbandHandler.class);

    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    // -------------------------------------------------------------------------------------------------
    //                                         /n create
    // -------------------------------------------------------------------------------------------------
    @Subcommand("create")
    @Description("Create a nation")
    public void onCreate(Player player, String name) {
        User user = User.get(player.getUniqueId());

        // CHECKS IF USER IS IN NATION ALREADY
        if (NationProfile.isInNation(user)) {
            Lang.ALREADY_IN_NATION.send(player);
            return;
        }

        // CHECKS IF NATION WITH SAME NAME ALREADY EXISTS
        if (instance.getNationManager().getNameNations().containsKey(name)) {
            Lang.NATION_NAME_ALREADY_EXISTS.send(player, "%NAME%;" + name);
            return;
        }

        String id = RandomStringUtils.randomAlphanumeric(7);

        // CHECKS IF NATION WITH SAME ID ALREADY EXISTS
        if (instance.getNationManager().getNations().containsKey(id)) {
            Lang.NATION_ID_ALREADY_EXISTS.send(player, "%NAME%;" + name);
            return;
        }

        // CHECKS IF NATION NAME IS ALPHANUMERIC
        if (!NationUtils.nameValid(name)) {
            Lang.NATION_NAME_INVALID.send(player);
            return;
        }


        Nation nation = new Nation(id, name, player.getUniqueId());
        nation.create(player);

        nation.addMember(user, NationRank.leader().getName());

        // TODO: open nation creation menu
        player.performCommand("nation gui");
    }

    // -------------------------------------------------------------------------------------------------
    //                                         /n rename
    // -------------------------------------------------------------------------------------------------
    @Subcommand("rename|name")
    @Description("Rename a nation")
    public void onRename(CommandSender sender, String name, @Optional String nationName) {
        // If sender is player
        if (sender instanceof Player player) {
            User user = User.get(player.getUniqueId());
            NationProfile profile = NationProfile.get(user);

            Nation nation = Nation.getFromName(nationName);

            // If the player just typed /disband with no extra args
            if (nationName == null || nationName.isEmpty()) {

                if (profile == null || nation == null) {
                    Lang.NOT_IN_NATION.send(player);
                    return;
                }

                boolean staffCondition = user.isBypassing() && player.hasPermission("nations.staff.forcerename");
                boolean playerCondition = profile.getNationRank().equals(NationRank.leader().getName());

                if (!(staffCondition || playerCondition)) {
                    Lang.NATION_NO_PERMISSION.send(player);
                    return;
                }

                if (!NationUtils.nameValid(name)) {
                    Lang.NATION_NAME_INVALID.send(sender);
                    return;
                }

            } else {
                // The player typed /disband <nation>

                if (nation == null) {
                    Lang.NATION_DOES_NOT_EXIST.send(player);
                    return;
                }

                boolean staffCondition = user.isBypassing() && player.hasPermission("nations.staff.forcerename");

                if (!staffCondition) {
                    Lang.NATION_NO_PERMISSION.send(player);
                    return;
                }

            }
            nation.rename(sender, name);

        } else {
            // If sender is not player
            if (nationName == null || nationName.isEmpty()) {
                Lang.NATION_MISSING_ARG.send(sender, "%X%;Nation Name");
                return;
            }

            if (!NationUtils.nameValid(name)) {
                Lang.NATION_NAME_INVALID.send(sender);
                return;
            }

            Nation nation = Nation.getFromName(nationName);
            if (nation == null) {
                Lang.NATION_DOES_NOT_EXIST.send(sender);
                return;
            }

            nation.rename(sender, name);
        }
    }

    // -------------------------------------------------------------------------------------------------
    //                                         /n disband
    // -------------------------------------------------------------------------------------------------
    @Subcommand("disband")
    @Description("Disband a nation")
    public void onDisband(CommandSender sender, @Optional String nationName) {
        if (sender instanceof Player player) {
            User user = User.get(player.getUniqueId());
            NationProfile profile = NationProfile.get(user);

            boolean staffCondition = user.isBypassing() && player.hasPermission("nations.staff.forcedisband");
            boolean playerCondition = profile != null ? profile.getNationRank().equals(NationRank.leader().getName()) : staffCondition;

            if (nationName == null || nationName.isEmpty()) {
                if (profile == null) {
                    Lang.NATION_MISSING_ARG.send(player, "%X%;Nation Name");
                    return;
                }
                if (Nation.getFromName(nationName) == null) {
                    Lang.NATION_DOES_NOT_EXIST.send(player);
                    return;
                }
            }

            Nation nation = Nation.getFromName(nationName);
            if (nation == null) {
                Lang.NATION_DOES_NOT_EXIST.send(player);
                return;
            }

            if (!staffCondition && !playerCondition) {
                Lang.NATION_NO_PERMISSION.send(player);
                return;
            }

            disbandHandler.getConfirmations().put(player.getUniqueId(), nationName);
            Lang.NATION_DISBAND_CONFIRM.send(player, "%NATION%;" + nation.getName());

        } else {
            if (nationName == null || nationName.isEmpty()) {
                Lang.NATION_MISSING_ARG.send(sender, "%X%;Nation Name");
                return;
            }
            Nation nation = Nation.getFromName(nationName);
            if (nation == null) {
                Lang.NATION_DOES_NOT_EXIST.send(sender);
                return;
            }

            nation.disband(sender);
        }
    }

    // -------------------------------------------------------------------------------------------------
    //                                         /n disband confirm
    // -------------------------------------------------------------------------------------------------
    @Subcommand("disband confirm")
    @Private
    @Description("Disband a nation confirm")
    public void onDisbandConfirm(Player player) {
        User user = User.get(player.getUniqueId());
        NationProfile profile = NationProfile.get(user);

        boolean staffCondition = user.isBypassing() && player.hasPermission("nations.staff.forcedisband");
        boolean playerCondition = profile.getNationRank().equals(NationRank.leader().getName());

        if (staffCondition || playerCondition) {
            if (disbandHandler.getConfirmations().containsKey(player.getUniqueId())) {
                Nation nation = Nation.getFromId(disbandHandler.getConfirmations().get(player.getUniqueId()));
                if (nation == null) return;
                nation.disband(player);
            } else {
                Lang.NATION_NO_NATION_TO_DISBAND.send(player);
            }
        } else {
            Lang.NATION_NO_PERMISSION.send(player);
        }
    }

    // -------------------------------------------------------------------------------------------------
    //                                         /n disband cancel
    // -------------------------------------------------------------------------------------------------
    @Subcommand("disband cancel")
    @Private
    @Description("Cancel a disbandment of a nation")
    public void onDisbandCancel(Player player) {
        if (disbandHandler.getConfirmations().containsKey(player.getUniqueId())) {
            disbandHandler.getConfirmations().remove(player.getUniqueId());
            Lang.NATION_DISBAND_CANCELLED.send(player);
        } else {
            Lang.NATION_NO_NATION_TO_DISBAND.send(player);
        }
    }

    // -------------------------------------------------------------------------------------------------
    //                                         /n description
    // -------------------------------------------------------------------------------------------------
    @Subcommand("description|desc|setdesc|setdescription")
    @Description("Set your nations description")
    public void onDescription(User user, String desc) {
        if (!NationProfile.isInNation(user)) {
            Lang.NOT_IN_NATION.send(user.getAsOfflinePlayer().getPlayer());
            return;
        }
        NationProfile nProf = NationProfile.get(user);
        Nation nation = nProf.getNation();
        if (!NationPermission.CHANGE_DESCRIPTION.hasPermission(user)) {
            Lang.NATION_NO_PERMISSION.send(user.getAsOfflinePlayer().getPlayer());
            return;
        }
        //TODO: No-No words
        nation.setDescription(desc);
        nation.save();
    }

}
