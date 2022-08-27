package net.pixlies.core.commands.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Single;
import net.pixlies.core.entity.user.User;
import org.bukkit.entity.Player;

public class UserCommand extends BaseCommand {

    @CommandAlias("user")
    @CommandPermission("pixlies.admin.user")
    public void onUser(Player player, @Single String arg) {
        User user = User.get(player.getUniqueId());
        player.sendMessage(user.toString());
        switch (arg.toLowerCase()) {
            case "save" -> {
                user.save();
                player.sendMessage("saved");
            }
            case "backup" -> {
                user.backup();
                player.sendMessage("backuped");
            }
            case "cache" -> {
                user.cache();
                player.sendMessage("cached");
            }
            case "removecache" -> {
                user.removeFromCache();
                player.sendMessage("removecache");
            }
            case "load" -> {
                user.load(true);
                player.sendMessage("saved");
            }
            case "loadfalse" -> {
                user.load(false);
                player.sendMessage("load");
            }
        }
    }

}
