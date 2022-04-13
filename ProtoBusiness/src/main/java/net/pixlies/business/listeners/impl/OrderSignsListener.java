package net.pixlies.business.listeners.impl;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.market.MarketProfile;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.apache.commons.lang.StringUtils;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OrderSignsListener implements Listener {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());

        if (user.getExtras().containsKey("marketProfile")) {
            Sign sign = (Sign) event.getBlock();
            String firstLine = String.valueOf(sign.line(0));
            MarketProfile profile = MarketProfile.get(user);
            assert profile != null;

            switch (profile.getSignStage()) {
                case 1 -> { // CHECKING ON THE AMOUNT
                    if (StringUtils.isNumeric(firstLine)) {
                        Order order = profile.getTempOrder();
                        order.setAmount(Integer.parseInt(firstLine));
                        OrderItem item = instance.getMarketManager().getBooks().get(order.getBookId()).getItem();
                        profile.openPricePage(item, order.getOrderType(), order.isLimitOrder(), order.getAmount());
                        profile.setTempOrder(null);
                        profile.setTempTitle(null);
                    } else {
                        user.getExtras().remove("marketProfile");
                        Lang.MARKET_NOT_A_VALID_AMOUNT.send(player);
                        player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
                    }
                }
                case 2 -> { // CHECKING ON THE PRICE
                    if (StringUtils.isNumeric(firstLine)) {
                        Order order = profile.getTempOrder();
                        order.setPrice(Double.parseDouble(firstLine));
                        profile.openConfirmOrderPage(order, profile.getTempTitle());
                        profile.setTempOrder(null);
                        profile.setTempTitle(null);
                    } else {
                        user.getExtras().remove("marketProfile");
                        Lang.MARKET_NOT_A_VALID_PRICE.send(player);
                        player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
                    }
                }
            }
        }
    }

}
