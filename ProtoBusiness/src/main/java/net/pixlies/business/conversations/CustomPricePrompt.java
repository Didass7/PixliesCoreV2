package net.pixlies.business.conversations;

import net.pixlies.business.guis.OrderConfirmGUI;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.Order;
import net.pixlies.business.market.OrderItem;
import net.pixlies.business.util.MarketUtil;
import net.pixlies.business.util.preconditions.ConversationPreconditions;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.UUID;

public class CustomPricePrompt extends NumericPrompt {
      @Override
      protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull Number input) {
            UUID uuid = (UUID) context.getSessionData("uuid");
            Order.Type type = (Order.Type) context.getSessionData("type");
            OrderItem item = OrderItem.valueOf((String) context.getSessionData("item"));
            int amount = (int) context.getSessionData("amount");
            
            Player player = Bukkit.getPlayer(uuid);
      
            // If the input is not a positive double
            if (!ConversationPreconditions.isPositiveDouble(player, input))
                  return Prompt.END_OF_CONVERSATION;
            
            BigDecimal totalPrice = MarketUtil.getTaxedPrice(uuid, input.doubleValue()).multiply(BigDecimal.valueOf(amount));
            
            // If the player does not have enough money
            if (!ConversationPreconditions.playerHasEnoughMoney(player, totalPrice.doubleValue()))
                  return Prompt.END_OF_CONVERSATION;
      
            MarketLang.ENTERED_A_PRICE.send(player, "%PRICE%;" + input.doubleValue());
            OrderConfirmGUI.open(uuid, type, item, amount, input.doubleValue());
            return Prompt.END_OF_CONVERSATION;
      }
      
      @Override
      public @NotNull String getPromptText(@NotNull ConversationContext context) {
            UUID uuid = (UUID) context.getSessionData("uuid");
            return MarketLang.ENTER_A_CUSTOM_PRICE.get(Bukkit.getPlayer(uuid));
      }
}
