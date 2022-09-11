package net.pixlies.nations.commands.impl.nations;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.nations.Nations;
import net.pixlies.nations.interfaces.NationProfile;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.ranks.NationRank;
import net.pixlies.nations.utils.NationUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.entity.Player;

@CommandAlias("%nationscommand")
@Subcommand("create")
public class NationCreateCommand extends BaseCommand {

    private static final Nations instance = Nations.getInstance();

    // -------------------------------------------------------------------------------------------------
    //                                         /n create
    // -------------------------------------------------------------------------------------------------
    @Default
    @Syntax("<name>")
    @Description("Create a nation")
    public void onCreate(Player player, String name) {
        NationProfile profile = NationProfile.get(player.getUniqueId());

        // CHECKS IF USER IS IN NATION ALREADY
        if (profile.isInNation()) {
            NationsLang.ALREADY_IN_NATION.send(player);
            return;
        }

        String id = RandomStringUtils.randomAlphanumeric(32);

        // CHECKS IF NATION WITH SAME NAME ALREADY EXISTS
        if (Nation.getFromName(name) != null || Nation.getFromId(id) != null) {
            NationsLang.NATION_ALREADY_EXISTS.send(player, "%NAME%;" + name);
            return;
        }

        // CHECKS IF NATION NAME IS ALPHANUMERIC
        if (!NationUtils.nameValid(name)) {
            NationsLang.NATION_NAME_INVALID.send(player);
            return;
        }

        Nation nation = new Nation(id, name, player.getUniqueId());
        nation.create(player);
        nation.addMember(player, NationRank.getLeaderRank().getName(), true);

        nation.cache();
        nation.save();

        // TODO: open nation creation menu
        player.performCommand("nation gui");
    }

}
