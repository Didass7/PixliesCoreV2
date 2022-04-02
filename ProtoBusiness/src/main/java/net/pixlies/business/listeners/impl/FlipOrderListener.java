package net.pixlies.business.listeners.impl;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.FlipOrderHandler;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.core.localization.Lang;
import org.apache.commons.lang.StringUtils;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class FlipOrderListener implements Listener {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private final FlipOrderHandler flipOrderHandler = instance.getHandlerManager().getHandler(FlipOrderHandler.class);

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        Sign sign = (Sign) event.getBlock();
        String firstLine = String.valueOf(sign.line(0));

        if (StringUtils.isNumeric(firstLine)) {
            Order flip = flipOrderHandler.getPendingFlips().get(player.getUniqueId())[1];
            OrderBook book = instance.getMarketManager().getBooks().get(flip.getBookId());
            book.sell(flip);

            Lang.ORDER_FLIPPED.send(player, "%AMOUNT%;" + flip.getAmount(), "%ITEM%;" + book.getItem().getName(),
                    "%COINS%;" + flip.getPrice());
            player.playSound(player.getLocation(), "block.amethyst_block.break", 100, 1);

            Order old = flipOrderHandler.getPendingFlips().get(player.getUniqueId())[0];
            instance.getMarketManager().getBooks().get(old.getBookId()).remove(old);
        } else {
            Lang.MARKET_NOT_A_VALID_PRICE.send(player);
            player.playSound(player.getLocation(), "block.netherite_block.place", 100, 1);
        }

        flipOrderHandler.removeFlip(player.getUniqueId());
    }

}
