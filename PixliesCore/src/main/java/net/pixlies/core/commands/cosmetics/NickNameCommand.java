package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.*;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("nickname|nick|disguise")
@CommandPermission("pixlies.cosmetics.nickname")
public class NickNameCommand extends BaseCommand {

    @Default
    @CommandCompletion("off")
    @Syntax("<nickname|off>")
    public void onNickName(CommandSender sender, String first, @Optional @Single String second) {
        boolean checkNickname =  first.length() < 3 || first.length() > 16 || !first.matches("^[a-zA-Z0-9_-]*$") || first.equals("NONE");

        // CONSOLE RENAME PLAYER / PLAYER RENAME PLAYER
        if (second != null) {

            // IF SENDER DOES NOT HAVE PERM TO NICK OTHERS
            if (!sender.hasPermission("pixlies.cosmetics.nickname.others")) {
                throw new InvalidCommandArgument(MessageKeys.PERMISSION_DENIED_PARAMETER);
            }

            Player target = Bukkit.getPlayer(second);
            if (target == null) {
                throw new InvalidCommandArgument(MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", second);
            }
            User targetUser = User.get(target.getUniqueId());

            // REMOVE NICKNAME
            if (first.equalsIgnoreCase("off")) {
                targetUser.removeNickName();
                Lang.COSMETICS_NICKNAME.send(sender, "%MESSAGE%;" + target.getName());
                return;
            }

            // CHECK IF NICKNAME IS ACCEPTED
            if (checkNickname) {
                Lang.COSMETICS_CANNOT_NICKNAME.send(sender);
                return;
            }

            // NICK COLOR
            String name = CC.format(first);

            // SET NICKNAME
            targetUser.setNickName(name);
            targetUser.save();
            Lang.COSMETICS_NICKNAME_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%MESSAGE%;" + name);

            return;
        }

        // RENAME SELF
        // IF SENDER IS A PLAYER CONTINUE
        if (!(sender instanceof Player player)) {
            throw new ConditionFailedException(MessageKeys.NOT_ALLOWED_ON_CONSOLE);
        }

        User user = User.get(player.getUniqueId());

        // REMOVE NICKNAME
        if (first.equalsIgnoreCase("off")) {
            user.removeNickName();
            Lang.COSMETICS_NICKNAME.send(player, "%MESSAGE%;" + player.getName());
            return;
        }

        // CHECK NICKNAME
        if (checkNickname) {
            Lang.COSMETICS_CANNOT_NICKNAME.send(player);
            return;
        }

        // NICKNAME COLOR W/ PERM
        String name = player.hasPermission("pixlies.cosmetics.nickname.color") ? CC.format(first) : first;

        // SET NICKNAME
        user.setNickName(name);
        user.save();
        Lang.COSMETICS_NICKNAME.send(player, "%MESSAGE%;" + name);
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
