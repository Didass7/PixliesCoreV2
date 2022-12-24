package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.handlers.impl.VanishHandler;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.ranks.Rank;
import net.pixlies.core.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.*;

public class ListCommand extends BaseCommand {

    @CommandAlias("list")
    @Description("Lists all online players")
    public void onList(CommandSender sender) {

        if (Bukkit.getOnlinePlayers().size() > 500) {
            sender.sendMessage(CC.format(Lang.PIXLIES + "&7We cannot show a list of more than 500 players."));
        }

        List<Rank> rankList = new ArrayList<>(Arrays.stream(Rank.values()).toList());
        Collections.reverse(rankList);

        StringJoiner joiner = new StringJoiner("&7, &f");
        for (Rank rank : rankList) {
            joiner.add(rank.getColor() + rank.getDisplayName());
        }


        StringJoiner playerJoiner = new StringJoiner("&7, &f");

        Bukkit.getOnlinePlayers()
                .stream()
                .sorted((a, b) -> -Rank.getRank(a.getUniqueId()).compareTo(Rank.getRank(b.getUniqueId())))
                .filter(p -> {
                    User user = User.get(p.getUniqueId());
                    return !user.isVanished() && !user.isPassive() && !user.isInStaffMode();
                })
                .toList().forEach(player -> {
                    User user = User.get(player.getUniqueId());
                    Rank rank = Rank.getRank(player.getUniqueId());
                    playerJoiner.add(rank.getColor() + user.getNickName());
                });

        int online = Bukkit.getOnlinePlayers().size() - Main.getInstance().getHandlerManager().getHandler(VanishHandler.class).getVanishedPlayers().size();

        StringBuilder builder = new StringBuilder()
                .append(CC.CHAT_STRIKETHROUGH)
                .append("\n")
                .append(CC.format(joiner.toString()))
                .append("\n \n")
                .append(CC.format("&8[&b" + online + "&8/&b" + Bukkit.getMaxPlayers() + "&8]"))
                .append("\n")
                .append(CC.format(playerJoiner.toString()))
                .append("\n")
                .append(CC.CHAT_STRIKETHROUGH);

        sender.sendMessage(builder.toString());

    }

}
