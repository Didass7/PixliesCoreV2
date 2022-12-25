package net.pixlies.nations.commands.impl.nations;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.user.User;
import net.pixlies.nations.Nations;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.timers.impl.NationDisbandTimer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@CommandAlias("%nationscommand")
@Subcommand("disband")
public class NationDisbandCommand extends BaseCommand {

    private static final Nations instance = Nations.getInstance();

    // -------------------------------------------------------------------------------------------------
    //                                         /n disband
    // -------------------------------------------------------------------------------------------------
    @Default
    @Description("Disband a nation")
    @Syntax(" ")
    @CommandCompletion("@empty")
    public void onDisband(CommandSender sender, @Optional String nationName) {

        // PLAYER
        if (sender instanceof Player player) {
            User user = User.get(player.getUniqueId());
            NationProfile profile = NationProfile.get(player.getUniqueId());

            if (user.getAllTimers().containsKey(NationDisbandTimer.ID)) {
                user.getAllTimers().remove(NationDisbandTimer.ID);
                NationsLang.NATION_DISBAND_CANCELLED.send(player);
                return;
            }

            boolean staffCondition = user.isBypassing() && player.hasPermission("nations.staff.forcedisband");
            boolean playerCondition = profile.isNationLeader();

            // :: /nation disband
            if (nationName == null || nationName.isEmpty()) {

                // NOT IN NATION
                if (!profile.isInNation()) {
                    NationsLang.NOT_IN_NATION.send(player);
                    return;
                }

                if (!(staffCondition || playerCondition)) {
                    // NO PERMISSION TO DISBAND
                    NationsLang.NATION_NO_PERMISSION.send(player);
                    return;
                }

                // STAFF OR PLAYER DISBAND
                Nation nation = profile.getNation();
                if (nation == null) {
                    NationsLang.NOT_IN_NATION.send(player);
                    return;
                }

                NationDisbandTimer timer = new NationDisbandTimer(System.currentTimeMillis(), nation.getNationId());
                user.getAllTimers().put(NationDisbandTimer.ID, timer);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!user.getAllTimers().containsKey(NationDisbandTimer.ID)) {
                            cancel();
                        }
                        if (timer.isExpired()) {
                            user.getAllTimers().remove(NationDisbandTimer.ID);
                            cancel();
                        }
                    }
                }.runTaskTimer(instance, 1, 1);

                NationsLang.NATION_DISBAND_CONFIRM.send(player, "%NATION%;" + nation.getName());
                return;

            }

            // :: /nation disband <NATION>
            if (!staffCondition) {
                throw new ConditionFailedException(MessageKeys.PERMISSION_DENIED_PARAMETER); // staff command, normal perm deny message
            }

            Nation nation = Nation.getFromName(nationName);
            if (nation == null) {
                NationsLang.NATION_DOES_NOT_EXIST.send(player);
                return;
            }

            NationDisbandTimer timer = new NationDisbandTimer(System.currentTimeMillis(), nation.getNationId());
            user.getAllTimers().put(NationDisbandTimer.ID, timer);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!user.getAllTimers().containsKey(NationDisbandTimer.ID)) {
                        cancel();
                    }
                    if (!user.isBypassing()) { // runnable, can be false
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

            NationsLang.NATION_DISBAND_CONFIRM.send(player, "%NATION%;" + nation.getName());

        } else {
            // CONSOLE
            if (nationName == null || nationName.isEmpty()) {
                NationsLang.NATION_MISSING_ARG.send(sender, "%X%;Nation Name");
                return;
            }
            Nation nation = Nation.getFromName(nationName);
            if (nation == null) {
                NationsLang.NATION_DOES_NOT_EXIST.send(sender);
                return;
            }

            nation.disband(sender);
        }
    }

    // -------------------------------------------------------------------------------------------------
    //                                         /n disband confirm
    // -------------------------------------------------------------------------------------------------
    @Subcommand("confirm")
    @Private
    @Description("Confirm disbanding a nation")
    public void onDisbandConfirm(Player player) {
        User user = User.get(player.getUniqueId());
        NationProfile profile = NationProfile.get(player.getUniqueId());

        boolean staffCondition = user.isBypassing() && player.hasPermission("nations.staff.forcedisband");
        boolean playerCondition = profile.isNationLeader();

        if (staffCondition || playerCondition) {
            if (user.getAllTimers().containsKey(NationDisbandTimer.ID)) {

                if (!(user.getAllTimers().get(NationDisbandTimer.ID) instanceof NationDisbandTimer timer)) {
                    user.getAllTimers().remove(NationDisbandTimer.ID);
                    NationsLang.NATION_NO_NATION_TO_DISBAND.send(player);
                    return;
                }
                user.getAllTimers().remove(NationDisbandTimer.ID);

                Nation nation = Nation.getFromId(timer.getNationToDisband());
                if (nation == null) {
                    NationsLang.NATION_NO_NATION_TO_DISBAND.send(player);
                    return;
                }
                nation.disband(player);
            } else {
                NationsLang.NATION_NO_NATION_TO_DISBAND.send(player);
            }
        } else {
            NationsLang.NATION_NO_PERMISSION.send(player);
        }

    }

    // -------------------------------------------------------------------------------------------------
    //                                         /n disband cancel
    // -------------------------------------------------------------------------------------------------
    @Subcommand("cancel")
    @Private
    @Description("Cancel a disbandment of a nation")
    public void onDisbandCancel(Player player) {
        User user = User.get(player.getUniqueId());
        if (user.getAllTimers().containsKey(NationDisbandTimer.ID)) {
            user.getAllTimers().remove(NationDisbandTimer.ID);
            NationsLang.NATION_DISBAND_CANCELLED.send(player);
        } else {
            NationsLang.NATION_NO_NATION_TO_DISBAND.send(player);
        }
    }

}
