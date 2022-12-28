package net.pixlies.nations.commands.impl.nations;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.user.User;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.ranks.NationPermission;
import net.pixlies.nations.utils.NationUtils;
<<<<<<< HEAD
import org.apache.commons.lang.StringUtils;
=======
import org.bukkit.Location;
>>>>>>> 1c13fa567a0441282f4243ce1537dfb76dda6bba
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("%nationscommand")
@Subcommand("rename")
public class NationRenameCommand extends BaseCommand {

    // -------------------------------------------------------------------------------------------------
    //                                         /n rename
    // -------------------------------------------------------------------------------------------------
    @Default
    @Syntax("<name>")
    @Description("Rename a nation")
    public void onRename(CommandSender sender, String name) {

        // If sender is player
        if (sender instanceof Player player) {
            User user = User.get(player.getUniqueId());
            NationProfile profile = NationProfile.get(player.getUniqueId());

            // If the player just typed /disband with no extra args
            Nation nation;
            // If player is currently bypassing territories
            if (user.isBypassing() && player.hasPermission("nations.staff.forcerename")) {
                String nationName = name.split(" ")[0];
                String newName = name.replaceFirst(nationName, "").trim();

                nation = Nation.getFromName(nationName);
                if (nation == null) {
                    NationsLang.NATION_DOES_NOT_EXIST.send(player);
                    return;
                }

                if (newName.isEmpty()) {
                    NationsLang.NATION_MISSING_ARG.send(player, "%X%;New Name");
                    return;
                }

<<<<<<< HEAD
                if (nation.getName().equalsIgnoreCase(name)) {
                    NationsLang.NATION_NAME_INVALID.send(sender);
                    return;
                }

                if (!NationUtils.nameValid(name)) {
                    NationsLang.NATION_NAME_INVALID.send(sender);
=======
                if (Nation.getFromName(newName) != null && !nation.getName().equalsIgnoreCase(newName) || nation.getName().equals(newName)) {
                    NationsLang.NATION_ALREADY_EXISTS.send(player);
>>>>>>> 1c13fa567a0441282f4243ce1537dfb76dda6bba
                    return;
                }

                if (!NationUtils.nameValid(newName)) {
                    NationsLang.NATION_NAME_INVALID.send(player);
                    return;
                }

                nation.rename(player, newName);
                nation.save();

                return;
            }
<<<<<<< HEAD
            nation.rename(sender, StringUtils.capitalize(name));
            nation.save();
=======
>>>>>>> 1c13fa567a0441282f4243ce1537dfb76dda6bba

            // If player is not bypassing
            nation = profile.getNation();
            if (nation == null) {
                NationsLang.NOT_IN_NATION.send(player);
                return;
            }

            boolean playerCondition = NationPermission.CHANGE_NAME.hasPermission(sender);

            if (!playerCondition) {
                NationsLang.NATION_NO_PERMISSION.send(sender);
                return;
            }

            if (Nation.getFromName(name) != null && !nation.getName().equalsIgnoreCase(name) || nation.getName().equals(name)) {
                NationsLang.NATION_ALREADY_EXISTS.send(player);
                return;
            }

            if (!NationUtils.nameValid(name)) {
                NationsLang.NATION_NAME_INVALID.send(player);
                return;
            }

            nation.rename(player, name);
            nation.save();

        } else {
            String nationName = name.split(" ")[0];
            String newName = name.replaceFirst(nationName, "").trim();

            Nation nation = Nation.getFromName(nationName);
            if (nation == null) {
                NationsLang.NATION_DOES_NOT_EXIST.send(sender);
                return;
            }

<<<<<<< HEAD
            nation.rename(sender, StringUtils.capitalize(name));
=======
            if (newName.isEmpty()) {
                NationsLang.NATION_MISSING_ARG.send(sender, "%X%;New Name");
                return;
            }

            if (Nation.getFromName(newName) != null && !nation.getName().equalsIgnoreCase(newName) || nation.getName().equals(newName)) {
                NationsLang.NATION_ALREADY_EXISTS.send(sender);
                return;
            }

            if (!NationUtils.nameValid(newName)) {
                NationsLang.NATION_NAME_INVALID.send(sender);
                return;
            }

            nation.rename(sender, newName);
>>>>>>> 1c13fa567a0441282f4243ce1537dfb76dda6bba
            nation.save();

        }
    }

}
