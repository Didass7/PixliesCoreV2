package net.pixlies.nations.integrations;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.pixlies.core.utils.CC;
import net.pixlies.nations.Nations;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.ranks.NationRank;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class NationsPlaceholderExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "nations";
    }

    @Override
    public @NotNull String getAuthor() {
        return "pixliesdevelopment";
    }

    @Override
    public @NotNull String getVersion() {
        return Nations.getInstance().getPluginVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("chat")) {
            if (!player.isOnline()) return "";
            NationProfile profile = NationProfile.get(player.getUniqueId());
            NationRank rank = profile.getRank();
            if (rank == null) return "";
            Nation nation = profile.getNation();
            if (nation == null) return "";

            return rank.getPrefix() + CC.format("&7&l") + nation.getName() + " "; // TODO: ALLY, NORMAL WHATEVER THING
        }
        return null;
    }

}
