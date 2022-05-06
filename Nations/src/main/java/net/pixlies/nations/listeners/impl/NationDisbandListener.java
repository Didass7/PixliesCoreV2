package net.pixlies.nations.listeners.impl;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.pixlies.core.localization.Lang;
import net.pixlies.nations.Nations;
import net.pixlies.nations.handlers.impl.NationDisbandHandler;
import net.pixlies.nations.nations.Nation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NationDisbandListener implements Listener {

    private static final Nations instance = Nations.getInstance();
    private final NationDisbandHandler handler = instance.getHandlerManager().getHandler(NationDisbandHandler.class);

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String message = event.message().toString();

        if (handler.getConfirmations().containsKey(player.getUniqueId())) {
            if (message.equals("confirm")) {

                String nationId = handler.getConfirmations().get(player.getUniqueId());

                Nation nation = Nation.getFromId(nationId);

                if (nation == null) {
                    return;
                }

                nation.disband(player);

            } else {
                Lang.NATION_DISBAND_CANCELLED.send(player);
            }
            handler.getConfirmations().remove(player.getUniqueId());
        }

    }

}
