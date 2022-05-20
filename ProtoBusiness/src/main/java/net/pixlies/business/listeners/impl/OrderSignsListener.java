package net.pixlies.business.listeners.impl;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.market.orders.OrderProfile;
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

        if (user.getExtras().containsKey("orderProfile")) {
            Sign sign = (Sign) event.getBlock();
            String firstLine = String.valueOf(sign.line(0));
            OrderProfile profile = OrderProfile.get(user);
            assert profile != null;

            switch (profile.getSignStage()) {
                case 1 -> { // CHECKING ON THE AMOUNT
                    if (StringUtils.isNumeric(firstLine)) {
                        Order order = profile.getTempOrder();
                        OrderItem item = instance.getMarketManager().getBooks().get(order.getBookId()).getItem();

                        if (Integer.parseInt(firstLine) > profile.getItemAmount(item)) {
                            Lang.MARKET_NOT_ENOUGH_ITEMS.send(player);
                            user.getExtras().remove("orderProfile");
                            player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
                            break;
                        }

                        order.setAmount(Integer.parseInt(firstLine));
                        profile.openPricePage(item, order.getType(), order.getAmount());
                        profile.setTempOrder(null);
                        profile.setTempTitle(null);
                    } else {
                        user.getExtras().remove("orderProfile");
                        Lang.MARKET_NOT_A_VALID_AMOUNT.send(player);
                        player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
                    }
                }
                case 2 -> { // CHECKING ON THE PRICE
                    if (StringUtils.isNumeric(firstLine)) {
                        // TODO: not enough money

                        Order order = profile.getTempOrder();
                        order.setPrice(Double.parseDouble(firstLine));
                        profile.openConfirmOrderPage(order, profile.getTempTitle());
                        profile.setTempOrder(null);
                        profile.setTempTitle(null);
                    } else {
                        user.getExtras().remove("orderProfile");
                        Lang.MARKET_NOT_A_VALID_PRICE.send(player);
                        player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
                    }
                }
            }
        }
    }

}
