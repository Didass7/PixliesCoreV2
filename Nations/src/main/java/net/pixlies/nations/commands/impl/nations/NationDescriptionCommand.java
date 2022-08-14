package net.pixlies.nations.commands.impl.nations;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.user.User;
import net.pixlies.nations.interfaces.NationProfile;
import net.pixlies.nations.locale.Lang;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.ranks.NationPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("%nationscommand")
@Subcommand("description")
public class NationDescriptionCommand extends BaseCommand {

    // -------------------------------------------------------------------------------------------------
    //                                         /n description
    // -------------------------------------------------------------------------------------------------
    @Default
    @Syntax("<description>")
    @Description("Set your nation's description")
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

                if (!(staffCondition || playerCondition)) {
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
