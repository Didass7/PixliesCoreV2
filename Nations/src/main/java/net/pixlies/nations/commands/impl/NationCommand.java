package net.pixlies.nations.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.nations.Nations;
import net.pixlies.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.ranks.NationPermission;
import net.pixlies.nations.nations.ranks.NationRank;
import net.pixlies.nations.nations.timers.impl.NationDisbandTimer;
import net.pixlies.nations.utils.NationUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@CommandAlias("nation|nations|n|faction|factions|f|country|countries")
public class NationCommand extends BaseCommand {

    private static final Nations instance = Nations.getInstance();

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
        NationProfile profile = NationProfile.get(player.getUniqueId());

        // CHECKS IF USER IS IN NATION ALREADY
        if (profile.isInNation()) {
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

        nation.addMember(player, NationRank.getLeaderRank().getName());

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
            NationProfile profile = NationProfile.get(player.getUniqueId());

            // If the player just typed /disband with no extra args
            Nation nation;
            if (nationName == null || nationName.isEmpty()) {

                nation = profile.getNation();
                if (nation == null) {
                    Lang.NOT_IN_NATION.send(player);
                    return;
                }

                boolean staffCondition = user.isBypassing() && player.hasPermission("nations.staff.forcerename");
                boolean playerCondition = NationPermission.CHANGE_NAME.hasPermission(sender);

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

                boolean staffCondition = user.isBypassing() && player.hasPermission("nations.staff.forcerename");
                if (!staffCondition) {
                    Lang.NATION_NO_PERMISSION.send(player);
                    return;
                }

                nation = Nation.getFromName(nationName);
                if (nation == null) {
                    Lang.NATION_DOES_NOT_EXIST.send(player);
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

        // PLAYER
        if (sender instanceof Player player) {
            User user = User.get(player.getUniqueId());
            NationProfile profile = NationProfile.get(player.getUniqueId());

            if (user.getAllTimers().containsKey(NationDisbandTimer.ID)) {
                user.getAllTimers().remove(NationDisbandTimer.ID);
                Lang.NATION_DISBAND_CANCELLED.send(player);
                return;
            }

            boolean staffCondition = user.isBypassing() && player.hasPermission("nations.staff.forcedisband");
            boolean playerCondition = profile.getNationRank() != null && profile.getNationRank().equals(NationRank.getLeaderRank().getName());

            // :: /nation disband
            if (nationName == null || nationName.isEmpty()) {

                // NOT IN NATION
                if (!profile.isInNation()) {
                    Lang.NOT_IN_NATION.send(player);
                    return;
                }

                if (!staffCondition || !playerCondition) {
                    // NO PERMISSION TO DISBAND
                    Lang.NATION_NO_PERMISSION.send(player);
                    return;
                }

                // STAFF OR PLAYER DISBAND
                Nation nation = profile.getNation();
                if (nation == null) {
                    Lang.NOT_IN_NATION.send(player);
                    return;
                }
                nation.disband(player);
                return;

            }

            // :: /nation disband <NATION>
            if (!staffCondition) {
                Lang.NATION_NO_PERMISSION.send(player);
                return;
            }

            Nation nation = Nation.getFromName(nationName);
            if (nation == null) {
                Lang.NATION_DOES_NOT_EXIST.send(player);
                return;
            }

            NationDisbandTimer timer = new NationDisbandTimer(System.currentTimeMillis(), nationName);
            user.getAllTimers().put(NationDisbandTimer.ID, timer);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!staffCondition) { // runnable, can be false
                        user.getAllTimers().remove(NationDisbandTimer.ID);
                        cancel();
                        return;
                    }
                    if (timer.isExpired()) {
                        user.getAllTimers().remove(NationDisbandTimer.ID);
                        cancel();
                    }
                }
            }.runTaskTimer(instance, 1, 1);

            Lang.NATION_DISBAND_CONFIRM.send(player, "%NATION%;" + nation.getName());

        } else {
            // CONSOLE
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
        NationProfile profile = NationProfile.get(player.getUniqueId());

        boolean staffCondition = user.isBypassing() && player.hasPermission("nations.staff.forcedisband");
        boolean playerCondition = profile.getNationRank() != null && profile.getNationRank().equals(NationRank.getLeaderRank().getName());

        if (staffCondition || playerCondition) {
            if (user.getAllTimers().containsKey(NationDisbandTimer.ID)) {

                if (!(user.getAllTimers().get(NationDisbandTimer.ID) instanceof NationDisbandTimer timer)) {
                    user.getAllTimers().remove(NationDisbandTimer.ID);
                    Lang.NATION_NO_NATION_TO_DISBAND.send(player);
                    return;
                }
                user.getAllTimers().remove(NationDisbandTimer.ID);

                Nation nation = Nation.getFromId(timer.getNationToDisband());
                if (nation == null) {
                    Lang.NATION_NO_NATION_TO_DISBAND.send(player);
                    return;
                }
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
        User user = User.get(player.getUniqueId());
        if (user.getAllTimers().containsKey(NationDisbandTimer.ID)) {
            user.getAllTimers().remove(NationDisbandTimer.ID);
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
    public void onDescription(CommandSender sender, String desc, @Optional String nationName) {

        // PLAYER
        if (sender instanceof Player player) {
            User user = User.get(player.getUniqueId());
            NationProfile profile = NationProfile.get(player.getUniqueId());

            boolean staffCondition = user.isBypassing() && player.hasPermission("nations.staff.forcedescription");
            boolean playerCondition = NationPermission.CHANGE_DESCRIPTION.hasPermission(player);

            // :: /nation description
            if (nationName == null || nationName.isEmpty()) {

                // NOT IN NATION
                if (!profile.isInNation()) {
                    Lang.NOT_IN_NATION.send(player);
                    return;
                }

                if (!staffCondition || !playerCondition) {
                    // NO PERMISSION TO DESC
                    Lang.NATION_NO_PERMISSION.send(player);
                    return;
                }

                // STAFF OR PLAYER DESC
                Nation nation = profile.getNation();
                if (nation == null) {
                    Lang.NOT_IN_NATION.send(player);
                    return;
                }

                nation.setDescription(desc);
                return;

            }

            // :: /nation description <DESC> <NATION>
            if (!staffCondition) {
                Lang.NATION_NO_PERMISSION.send(player);
                return;
            }

            Nation nation = Nation.getFromName(nationName);
            if (nation == null) {
                Lang.NATION_DOES_NOT_EXIST.send(player);
                return;
            }

            nation.setDescription(desc);

        } else {
            // CONSOLE
            if (nationName == null || nationName.isEmpty()) {
                Lang.NATION_MISSING_ARG.send(sender, "%X%;Nation Name");
                return;
            }
            Nation nation = Nation.getFromName(nationName);
            if (nation == null) {
                Lang.NATION_DOES_NOT_EXIST.send(sender);
                return;
            }

            nation.setDescription(desc);

        }

    }
}
