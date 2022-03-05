package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("nickname|nick|disguise")
@CommandPermission("pixlies.cosmetics.nickname")
public class NickNameCommand extends BaseCommand {

    @Default
    @CommandCompletion("off")
    public void onNickName(Player player, @Optional String string) {
        User user = User.get(player.getUniqueId());
        if (string == null || string.equalsIgnoreCase("off")) {
            user.removeNickName();
            Lang.COSMETICS_NICKNAME.send(player, "%MESSAGE%" + player.getName());
            return;
        }
        if (string.isEmpty() || string.length() > 16 || string.matches("^[a-zA-Z0-9_-]*$") || string.equals("NONE")) {
            Lang.COSMETICS_CANNOT_NICKNAME.send(player);
            return;
        }
        String name = player.hasPermission("pixlies.cosmetics.nickname.color") ? CC.format(string) : string;
        user.setNickName(name);
        Lang.COSMETICS_NICKNAME.send(player, "%MESSAGE%;" + name);
    }

    @Private
    @CommandCompletion("@players off")
    @CommandPermission("pixlies.cosmetics.nickname.others")
    public void onNickName(CommandSender sender, User user, @Optional String string) {
        if (string == null || string.equalsIgnoreCase("off")) {
            user.removeNickName();
            Lang.COSMETICS_NICKNAME.send(sender, "%MESSAGE%" + user.getAsOfflinePlayer().getName());
            return;
        }
        if (string.isEmpty() || string.length() > 16 || string.matches("^[a-zA-Z0-9_-]*$") || string.equals("NONE")) {
            Lang.COSMETICS_CANNOT_NICKNAME.send(sender);
            return;
        }
        String name = CC.format(string);
        user.setNickName(name);
        Lang.COSMETICS_NICKNAME_OTHER.send(sender, "%PLAYER%;" + user.getAsOfflinePlayer().getName(), "%MESSAGE%;" + name);
    }

}
