package net.pixlies.business.listeners.impl;

/*
TODO REVAMP EVERYTHING
 */
/*
public class OrderSignsListener implements Listener {
    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);
    
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        
        if (!marketHandler.getProfiles().containsKey(player.getUniqueId())) return;
        
        Sign sign = (Sign) event.getBlock().getState();
        String firstLine = String.valueOf(sign.line(0));
        OrderProfile profile = OrderProfile.get(player.getUniqueId());
        assert profile != null;
        
        if (!StringUtils.isNumericSpace(firstLine)) {
            player.closeInventory();
            MarketLang.MARKET_NOT_A_VALID_AMOUNT.send(player);
            player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
            return;
        }
        
        switch (profile.getSignStage()) {
            
            // CHECKING ON THE AMOUNT
            case 1 -> {
                Order order = profile.getTempOrder();
                OrderItem item = OrderBook.get(order.getBookItem()).getItem();
                
                if (order.getType() == Order.Type.SELL && Integer.parseInt(firstLine) > profile.getItemAmount(item)) {
                    player.closeInventory();
                    MarketLang.MARKET_NOT_ENOUGH_ITEMS.send(player);
                    player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
                    return;
                }
                
                order.setAmount(Integer.parseInt(firstLine));
                // event.getBlock().setType(Material.AIR);
                profile.openPricePage(item, order.getType(), order.getAmount());
                profile.setTempOrder(null);
                profile.setTempTitle(null);
            }
            
            // CHECKING ON THE PRICE
            case 2 -> {
                // TODO: not enough money
                
                Order order = profile.getTempOrder();
                order.setPrice(Double.parseDouble(firstLine));
                event.getBlock().setType(Material.AIR);
                profile.openConfirmOrderPage(order, profile.getTempTitle());
                profile.setTempOrder(null);
                profile.setTempTitle(null);
            }
        }
    }
}

 */
