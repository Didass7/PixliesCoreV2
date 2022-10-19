package net.pixlies.nations.commands.impl.nations;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.user.User;
import net.pixlies.nations.interfaces.NationProfile;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.ranks.NationPermission;
import net.pixlies.nations.utils.NationUtils;
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
                    NationsLang.NOT_IN_NATION.send(player);
                    return;
                }

                boolean staffCondition = user.isBypassing() && player.hasPermission("nations.staff.forcerename");
                boolean playerCondition = NationPermission.CHANGE_NAME.hasPermission(sender);

                if (!(staffCondition || playerCondition)) {
                    NationsLang.NATION_NO_PERMISSION.send(player);
                    return;
                }

                if (!NationUtils.nameValid(name)) {
                    NationsLang.NATION_NAME_INVALID.send(sender);
                    return;
                }
            } else {
                // The player typed /disband <nation>

                boolean staffCondition = user.isBypassing() && player.hasPermission("nations.staff.forcerename");
                if (!staffCondition) {
                    NationsLang.NATION_NO_PERMISSION.send(player);
                    return;
                }

                nation = Nation.getFromName(nationName);
                if (nation == null) {
                    NationsLang.NATION_DOES_NOT_EXIST.send(player);
                    return;
                }

            }
            nation.rename(sender, name);
            nation.save();


        } else {
            // If sender is not player
            if (nationName == null || nationName.isEmpty()) {
                NationsLang.NATION_MISSING_ARG.send(sender, "%X%;Nation Name");
                return;
            }

            if (!NationUtils.nameValid(name)) {
                NationsLang.NATION_NAME_INVALID.send(sender);
                return;
            }

            Nation nation = Nation.getFromName(nationName);
            if (nation == null) {
                NationsLang.NATION_DOES_NOT_EXIST.send(sender);
                return;
            }

            nation.rename(sender, name);
            nation.save();
        }
    }

}
