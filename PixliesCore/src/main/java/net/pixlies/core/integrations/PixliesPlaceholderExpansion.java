package net.pixlies.core.integrations;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.pixlies.core.Main;
import net.pixlies.core.ranks.Rank;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PixliesPlaceholderExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "pixlies";
    }

    @Override
    public @NotNull String getAuthor() {
        return "pixliesdevelopment";
    }

    @Override
    public @NotNull String getVersion() {
        return Main.getInstance().getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("prefix")) {
            return Rank.getRank(player.getUniqueId()).getPrefix();
        }

        if (params.equalsIgnoreCase("chatprefix")) {
            return Rank.getRank(player.getUniqueId()).getChatPrefix();
        }

        if (params.equalsIgnoreCase("suffix")) {
            return Rank.getRank(player.getUniqueId()).getSuffix();
        }

        if (params.equalsIgnoreCase("chatsuffix")) {
            return Rank.getRank(player.getUniqueId()).getChatSuffix();
        }
        return null;
    }

}
