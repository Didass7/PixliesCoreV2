package net.pixlies.nations.listeners.impl;

import net.pixlies.nations.commands.CommandManager;
import net.pixlies.nations.events.impl.PlayerTerritoryChangeEvent;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.chunk.NationChunk;
import net.pixlies.nations.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.relations.Relation;
import net.pixlies.nations.utils.NationTextUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TerritoryChangeListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onChange(PlayerTerritoryChangeEvent event) {
        Player player = event.getPlayer();

        NationProfile profile = NationProfile.get(player.getUniqueId());
        NationChunk to = event.getTo();

        if (to == null) {
            player.sendTitle(NationsLang.NATION_WILDERNESS_TITLE.get(player), NationsLang.NATION_WILDERNESS_SUBTITLE.get(player));
            return;
        }

        switch (to.getNationId()) {
           case "spawn" ->
                    player.sendTitle(NationsLang.NATION_SPAWN_TITLE.get(player), NationsLang.NATION_SPAWN_SUBTITLE.get(player));
            case "warp" ->
                    player.sendTitle(NationsLang.NATION_WARP_TITLE.get(player), NationsLang.NATION_WARP_SUBTITLE.get(player));
            case "warzone" ->
                    player.sendTitle(NationsLang.NATION_WARZONE_TITLE.get(player), NationsLang.NATION_WARZONE_SUBTITLE.get(player));
            default -> {
                Nation nation = to.getNation();
                if (nation == null) {
                    player.sendTitle(NationsLang.NATION_WILDERNESS_TITLE.get(player), NationsLang.NATION_WILDERNESS_SUBTITLE.get(player));
                    return;
                }

                Relation relation = profile.getRelationTo(nation);

                String nationName = relation.getColor() + nation.getName();
                String description = NationsLang.NATION_SUBTITLE_COLOUR.get() + nation.getDescription();

                player.sendTitle(nationName, description);
            }
        }
    }

}
