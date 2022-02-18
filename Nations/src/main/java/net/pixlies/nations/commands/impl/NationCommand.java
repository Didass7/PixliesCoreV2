package net.pixlies.nations.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.nations.Nations;
import net.pixlies.nations.handlers.impl.NationDisbandHandler;
import net.pixlies.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.customization.GovernmentType;
import net.pixlies.nations.nations.customization.Ideology;
import net.pixlies.nations.nations.customization.NationConstitution;
import net.pixlies.nations.nations.customization.Religion;
import net.pixlies.nations.nations.ranks.NationRank;
import net.pixlies.nations.utils.NationUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CommandAlias("nation|nations|n|faction|factions|country|countries")
public class NationCommand extends BaseCommand {

    private static final Nations instance = Nations.getInstance();
    private final NationDisbandHandler disbandHandler = instance.getHandlerManager().getHandler(NationDisbandHandler.class);

    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

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

        final List<Integer> ncValues = new ArrayList<>();
        for (NationConstitution nc : NationConstitution.values()) {
            ncValues.add(nc.getDefaultValue());
        }

        Nation nation = new Nation(
                id,
                name,
                NationUtils.randomDesc(),
                player.getUniqueId(),
                System.currentTimeMillis(),
                0.0,
                0.0,
                GovernmentType.UNITARY,
                Ideology.TRIBAL,
                Religion.SECULAR,
                ncValues,
                new HashMap<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        nation.create();
        Lang.NATION_FORMED.broadcast("%NATION%;" + nation.getName(), "%PLAYER%;" + player.getName());

        nation.addMember(user, NationRank.leader().getName());

        // TODO: open nation creation menu
    }

    @Subcommand("rename")
    @Description("Rename a nation")
    public void onRename(CommandSender sender, String name, @Optional String nationName) {
        if (sender instanceof Player player) {
            User user = User.get(player.getUniqueId());
            NationProfile profile = NationProfile.get(user);

            if (nationName == null || nationName.isEmpty()) {
                if (profile == null) {
                    Lang.NOT_IN_NATION.send(player);
                    return;
                }

                boolean staffCondition = user.getSettings().isBypassing() && player.hasPermission("nations.staff.forcerename");
                boolean playerCondition = profile.getNationRank().equals(NationRank.leader().getName());

                if (!(staffCondition || playerCondition)) {
                    Lang.NATION_NO_PERMISSION.send(player);
                    return;
                }

                Nation nation = Nation.getFromId(profile.getNationId());

                if (name.length() > 16 || name.matches("^[a-zA-Z0-9_-]*$") ) {
                    Lang.NATION_CANNOT_RENAME.send(sender);
                    return;
                }

                nation.rename(sender, name);
            } else {
                Nation nation = Nation.getFromName(nationName);

                if (nation == null) {
                    Lang.NATION_DOES_NOT_EXIST.send(player);
                    return;
                }

                boolean staffCondition = user.getSettings().isBypassing() && player.hasPermission("nations.staff.forcerename");

                if (!staffCondition) {
                    Lang.NATION_NO_PERMISSION.send(player);
                    return;
                }

                nation.rename(sender, name);
            }

        } else {
            if (nationName == null || nationName.isEmpty()) {
                Lang.NATION_MISSING_ARG.send(sender, "%X%;Nation Name");
                return;
            }

            if (name.length() > 16 || name.matches("^[a-zA-Z0-9_-]*$") ) {
                Lang.NATION_CANNOT_RENAME.send(sender);
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

    @Subcommand("disband")
    @Description("Disband a nation")
    public void onDisband(CommandSender sender, @Optional String nationName) {
        if (sender instanceof Player player) {
            User user = User.get(player.getUniqueId());
            NationProfile profile = NationProfile.get(user);

            if (nationName == null || nationName.isEmpty()) {
                if (profile == null) {
                    Lang.NATION_MISSING_ARG.send(player, "%X%;Nation Name");
                    return;
                } else {
                    if (Nation.getFromName(nationName) == null) {
                        Lang.NATION_DOES_NOT_EXIST.send(player);
                        return;
                    }
                    nationName = profile.getNationId();
                }
            }

            boolean staffCondition = user.getSettings().isBypassing() && player.hasPermission("nations.staff.forcedisband");
            boolean playerCondition = profile != null ? profile.getNationRank().equals(NationRank.leader().getName()) : staffCondition;

            if (staffCondition || playerCondition) {
                disbandHandler.getConfirmations().put(player.getUniqueId(), nationName);
                Lang.NATION_DISBAND_CONFIRM.send(player, "%NATION%;" +
                        Nation.getFromId(nationName).getName());
            } else {
                Lang.NATION_NO_PERMISSION.send(player);
            }
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

    @Subcommand("disband confirm")
    @Description("Disband a nation confirm")
    public void onDisbandConfirm(Player player) {
        User user = User.get(player.getUniqueId());
        NationProfile profile = (NationProfile) user.getExtras().get("nationsProfile");

        boolean staffCondition = user.getSettings().isBypassing() && player.hasPermission("nations.staff.forcedisband");
        boolean playerCondition = profile.getNationRank().equals(NationRank.leader().getName());

        if (staffCondition || playerCondition) {
            if (disbandHandler.getConfirmations().containsKey(player.getUniqueId())) {
                Nation nation = Nation.getFromId(disbandHandler.getConfirmations().get(player.getUniqueId()));
                nation.disband(player);
            } else {
                Lang.NATION_NO_NATION_TO_DISBAND.send(player);
            }
        } else {
            Lang.NATION_NO_PERMISSION.send(player);
        }
    }

    @Subcommand("disband cancel")
    @Description("Cancel a disbandment of a nation")
    public void onDisbandCancel(Player player) {
        if (disbandHandler.getConfirmations().containsKey(player.getUniqueId())) {
            disbandHandler.getConfirmations().remove(player.getUniqueId());
            Lang.NATION_DISBAND_CANCELLED.send(player);
        } else {
            Lang.NATION_NO_NATION_TO_DISBAND.send(player);
        }
    }

}
