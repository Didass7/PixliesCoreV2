package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("nickname|nick|disguise")
@CommandPermission("pixlies.cosmetics.nickname")
public class NickNameCommand extends BaseCommand {

    @Default
    public void onNickName(Player player, String string) {
        User user = User.get(player.getUniqueId());
        if (string.isEmpty() || string.length() > 16) {
            Lang.COSMETICS_CANNOT_NICKNAME.send(player);
            return;
        }
        String name = player.hasPermission("pixlies.cosmetics.nickname.color") ? CC.format(string) : string;
        user.setNickName(name);
        Lang.COSMETICS_NICKNAME.send(player, "%MESSAGE%;" + name);
    }

    @Private
    @CommandCompletion("@players")
    @CommandPermission("pixlies.cosmetics.nickname.others")
    public void onNickName(CommandSender sender, User user, String string) {
        if (string.isEmpty() || string.length() > 16) {
            Lang.COSMETICS_CANNOT_NICKNAME.send(sender);
            return;
        }
        String name = CC.format(string);
        user.setNickName(name);
        Lang.COSMETICS_NICKNAME_OTHER.send(sender, "%PLAYER%;" + user.getAsOfflinePlayer().getName(), "%MESSAGE%;" + name);
    }

}
