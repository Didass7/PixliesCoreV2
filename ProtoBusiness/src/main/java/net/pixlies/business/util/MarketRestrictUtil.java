package net.pixlies.business.util;

import net.pixlies.business.locale.MarketLang;
import net.pixlies.core.ranks.Rank;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MarketRestrictUtil {
    public static void sendRestrictMessage(Player player, OfflinePlayer target) {
        MarketLang.MARKET_PLAYER_RESTRICTED_SENDER.send(
                player,
                "%PLAYER%;" + Rank.getRank(target.getUniqueId()).getColor() + target.getName()
        );
        
        if (target.isOnline()) {
            MarketLang.MARKET_PLAYER_RESTRICTED_TARGET.send(
                    target.getPlayer(),
                    "%PLAYER%;" + Rank.getRank(player.getUniqueId()).getColor() + player.getName()
            );
            SoundUtil.grandError(target.getPlayer());
        }
    }
    
    public static void sendUnrestrictMessage(Player player, OfflinePlayer target) {
        MarketLang.MARKET_PLAYER_ALLOWED_SENDER.send(
                player,
                "%PLAYER%;" + Rank.getRank(target.getUniqueId()).getColor() + target.getName()
        );
        
        if (target.isOnline()) {
            MarketLang.MARKET_PLAYER_ALLOWED_TARGET.send(target.getPlayer());
            SoundUtil.grandSuccess(target.getPlayer());
        }
    }
}
