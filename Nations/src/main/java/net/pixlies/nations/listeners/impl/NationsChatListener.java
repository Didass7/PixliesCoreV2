package net.pixlies.nations.listeners.impl;

import net.pixlies.core.entity.user.User;
import net.pixlies.core.ranks.Rank;
import net.pixlies.core.utils.CC;
import net.pixlies.core.utils.ChatUtils;
import net.pixlies.nations.Nations;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.interfaces.profile.ChatType;
import net.pixlies.nations.nations.ranks.NationRank;
import net.pixlies.nations.nations.relations.Relation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class NationsChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onChatLowest(@SuppressWarnings("deprecation") AsyncPlayerChatEvent event) { // legacy chat formatting
        // TODO: Chat spying
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        NationProfile profile = NationProfile.get(player.getUniqueId());
        ChatType chatType = profile.getChatType();
        Nation nation = profile.getNation();
        String message = event.getMessage();

        if (chatType == ChatType.NATION && nation != null) {

            for (Player target : nation.getOnlineMembersAsPlayer()) {
                target.sendMessage(NationsLang.NATION_SAME_CHAT_FORMAT.get()
                        .replace("%PLAYER%", user.getNickName())
                        .replace("%MESSAGE%", message));
            }
            event.setCancelled(true);

        } else if (chatType == ChatType.ALLY && nation != null) {
            nation.getAlliedNations().forEach(alliedNation -> {
                // Send a message to all online allied players
                for (Player target : alliedNation.getOnlineMembersAsPlayer()) {
                    target.sendMessage(NationsLang.NATION_ALLY_CHAT_FORMAT.get()
                            .replace("%NATION%", nation.getName())
                            .replace("%PLAYER%", user.getNickName())
                            .replace("%MESSAGE%", message));
                }
            });
            event.setCancelled(true);
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(@SuppressWarnings("deprecation") AsyncPlayerChatEvent event) { // legacy chat formatting
        // Faction relations
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        Rank rank = Rank.getRank(player.getUniqueId());

        String format = "%NATION%%RANK%%PLAYER%&7: &f%MESSAGE%"; // EDIT THIS TO CHANGE FORMAT
        String message = ChatUtils.formatByPerm(player, event.getMessage());

        NationProfile profile = NationProfile.get(player.getUniqueId());
        Nation nation = profile.getNation();
        NationRank nationRank = profile.getRank();

        if (nation == null) {
            String toSendFormat = "%CHAT_PREFIX%%PLAYER%%CHAT_SUFFIX%&7: &f%MESSAGE%"
                    .replace("%CHAT_PREFIX%", rank.getChatPrefix())
                    .replace("%PLAYER%", user.getNickName())
                    .replace("%CHAT_SUFFIX%", rank.getChatSuffix());

            event.setFormat(CC.format(toSendFormat)
                    .replace("%MESSAGE%", message));
            return;
        }

        for (Player recipient : event.getRecipients()) {
            NationProfile recipientProfile = NationProfile.get(recipient.getUniqueId());
            Relation relationToSender = recipientProfile.getRelationTo(profile);

            String toSend = format
                    .replace("%NATION%", "&8[" + relationToSender.getColor() + nation.getName() + "&8] ")
                    .replace("%RANK%", nationRank == null ? "" : nationRank.getPrefix())
                    .replace("%PLAYER%", rank.getChatPrefix() + user.getNickName() + rank.getChatSuffix());

            recipient.sendMessage(CC.format(toSend)
                    .replace("%MESSAGE%", message));
        }

        event.getRecipients().clear();
    }

}
