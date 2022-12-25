package net.pixlies.lobby.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.TextUtils;
import net.pixlies.lobby.entity.LobbySpawn;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends BaseCommand {

    @CommandAlias("setspawn")
    @CommandPermission("pixlies.staff.setspawn")
    public void onSetSpawn(Player player) {
        Location location = player.getLocation();
        LobbySpawn spawn = LobbySpawn.getSpawn();

        spawn.setLocation(location);
        spawn.save();

        Lang.STAFF_SETSPAWN.send(player, "%TARGET%;" + TextUtils.getLocationFormatted(location));
    }

}
