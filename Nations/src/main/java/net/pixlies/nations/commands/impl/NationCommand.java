package net.pixlies.nations.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.TextUtils;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.customization.GovernmentType;
import net.pixlies.nations.nations.customization.Ideology;
import net.pixlies.nations.nations.customization.NationConstitution;
import net.pixlies.nations.nations.customization.Religion;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CommandAlias("nation|nations|n|faction|factions|country|countries")
public class NationCommand extends BaseCommand {

    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("create")
    @Description("Creates a nation")
    public void onCreate(Player player, String name) {
        User user = User.get(player.getUniqueId());

        /*
        if (user.inNation()) {
            Lang.ALREADY_IN_NATION.send(player);
            return;
        }

        */

        /* TODO (checks)
             - if nation with name already exists
             - if nation name is invalid
         */

        final String id = TextUtils.generateId(7);

        final List<Integer> ncValues = new ArrayList<>();

        for (NationConstitution nc : NationConstitution.values()) {
            ncValues.add(nc.getDefaultValue());
        }

        Nation nation = new Nation(id, name, "No description yet", player.getUniqueId(), System.currentTimeMillis(), 0.0, 0.0, GovernmentType.UNITARY, Ideology.TRIBAL, Religion.SECULAR, ncValues, new ArrayList<>(), new HashMap<>(), new ArrayList<>());
        nation.create();
        Lang.NATION_FORMED.broadcast("%NATION%;" + nation.getName(), "%PLAYER%;" + player.getName());

        // TODO: add user to nation as rank leader
        // TODO: open nation creation menu
    }

}
