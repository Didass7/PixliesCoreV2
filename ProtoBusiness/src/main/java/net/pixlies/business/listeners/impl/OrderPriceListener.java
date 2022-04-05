package net.pixlies.business.listeners.impl;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.apache.commons.lang.StringUtils;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OrderPriceListener implements Listener {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());

        if (user.getExtras().containsKey("marketProfile")) {
            Sign sign = (Sign) event.getBlock();
            String firstLine = String.valueOf(sign.line(0));

            if (StringUtils.isNumeric(firstLine)) {
                // TODO
            } else {
                user.getExtras().remove("marketProfile");
                Lang.MARKET_NOT_A_VALID_PRICE.send(player);
                player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
            }
        }
    }

}
