package net.pixlies.nations.listeners.impl;

import net.pixlies.nations.events.impl.PlayerTerritoryChangeEvent;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TerritoryChangeListener implements Listener {

    @EventHandler
    public void onChange(PlayerTerritoryChangeEvent event) {
        Player player = event.getPlayer();

        switch (event.getTo().getNationId()) {
            case "WILDERNESS" ->
                    player.sendTitle(NationsLang.NATION_WILDERNESS_TITLE.get(player), NationsLang.NATION_WILDERNESS_SUBTITLE.get(player));
            case "SPAWN" ->
                    player.sendTitle(NationsLang.NATION_SPAWN_TITLE.get(player), NationsLang.NATION_SPAWN_SUBTITLE.get(player));
            case "WARP" ->
                    player.sendTitle(NationsLang.NATION_WARP_TITLE.get(player), NationsLang.NATION_WARP_SUBTITLE.get(player));
            case "WARZONE" ->
                    player.sendTitle(NationsLang.NATION_WARZONE_TITLE.get(player), NationsLang.NATION_WARZONE_SUBTITLE.get(player));
            default -> {
                // TODO: COLOURS BASED ON RELATION
                if (NationProfile.get(player.getUniqueId()).isInNation()) {

                } else {

                }
                player.sendTitle(event.getTo().getNation().getName(), "§9" + event.getTo().getNation().getDescription());
            }
        }
    }

}
