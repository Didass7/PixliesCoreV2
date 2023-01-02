package net.pixlies.nations.utils;

import net.pixlies.core.utils.CC;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.chunk.NationChunk;
import net.pixlies.nations.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.ranks.NationPermission;
import net.pixlies.nations.nations.relations.Relation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * Different utilities for nations
 *
 * @author MickMMars
 */
public final class NationUtils {

    public static String randomDescription() {
        String[] arr = new String[] {
            "No description yet",
            "Use /n description to change me!",
            "Pixlies has a place for you",
            "Be creative!"
        };

        Random r = new Random();
        int randomNumber = r.nextInt(arr.length);

        return arr[randomNumber];
    }

    public static boolean nameValid(String name) {
        if (name == null) return false;
        if (name.length() > 16 || name.length() < 4) return false;
        if (!name.matches("^[a-zA-Z0-9]*$")) return false;
        return !name.equalsIgnoreCase("confirm");
    }

    public static String getColorNameFromNationChunk(Player player, @Nullable NationChunk nationChunk) {
        if (nationChunk == null) {
            return NationsLang.NATION_WILDERNESS_TITLE.get(player);
        }

        NationProfile profile = NationProfile.get(player.getUniqueId());
        switch (nationChunk.getNationId()) {
            case "spawn" -> {
                return NationsLang.NATION_SPAWN_TITLE.get(player);
            }
            case "warp" -> {
                return NationsLang.NATION_WARP_TITLE.get(player);
            }
            case "warzone" -> {
                return NationsLang.NATION_WARZONE_TITLE.get(player);
            }
            default -> {
                Nation nation = nationChunk.getNation();
                if (nation == null) {
                    return NationsLang.NATION_WILDERNESS_TITLE.get(player);
                }

                Relation relation = profile.getRelationTo(nation);

                return relation.getColor() + nation.getName();
            }
        }
    }

    public static String getColorDescFromNationChunk(Player player, @Nullable NationChunk nationChunk) {
        if (nationChunk == null) {
            return NationsLang.NATION_WILDERNESS_SUBTITLE.get(player);
        }

        switch (nationChunk.getNationId()) {
            case "spawn" -> {
                return NationsLang.NATION_SPAWN_SUBTITLE.get(player);
            }
            case "warp" -> {
                return NationsLang.NATION_WARP_SUBTITLE.get(player);
            }
            case "warzone" -> {
                return NationsLang.NATION_WARZONE_SUBTITLE.get(player);
            }
            default -> {
                Nation nation = nationChunk.getNation();
                if (nation == null) {
                    return NationsLang.NATION_WILDERNESS_SUBTITLE.get(player);
                }

                return nation.getDescription();
            }
        }
    }

    public static String getRelationColorFromNationChunk(Player player, @Nullable NationChunk nationChunk) {
        if (nationChunk == null) {
            return NationsLang.NATION_WILDERNESS_COLOR.get(player);
        }

        NationProfile profile = NationProfile.get(player.getUniqueId());
        switch (nationChunk.getNationId()) {
            case "spawn" -> {
                return NationsLang.NATION_SPAWN_COLOR.get(player);
            }
            case "warp" -> {
                return NationsLang.NATION_WARP_COLOR.get(player);
            }
            case "warzone" -> {
                return NationsLang.NATION_WARZONE_COLOR.get(player);
            }
            default -> {
                Nation nation = nationChunk.getNation();
                if (nation == null) {
                    return NationsLang.NATION_WILDERNESS_COLOR.get(player);
                }

                Relation relation = profile.getRelationTo(nation);

                return relation.getColor();
            }
        }
    }

}
