package net.pixlies.business.conversations;

import net.pixlies.business.guis.OrderPriceGUI;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.business.util.preconditions.ConversationPreconditions;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class AmountPrompt extends NumericPrompt {
      @Override
      protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull Number input) {
            UUID uuid = (UUID) context.getSessionData("uuid");
            Order.Type type = (Order.Type) context.getSessionData("type");
            OrderItem item = OrderItem.valueOf((String) context.getSessionData("item"));
            
            Player player = Bukkit.getPlayer(uuid);
      
            // If the input is not a positive double
            if (!ConversationPreconditions.isPositiveInteger(player, input))
                  return Prompt.END_OF_CONVERSATION;
            
            int amount = input.intValue();
            
            // If the player does not have the amount to sell
            if (type == Order.Type.SELL && !ConversationPreconditions.playerHasEnoughItems(player, item, amount))
                  return Prompt.END_OF_CONVERSATION;
            
            OrderPriceGUI.open(uuid, type, item, amount);
            return Prompt.END_OF_CONVERSATION;
      }
      
      @Override
      public @NotNull String getPromptText(@NotNull ConversationContext context) {
            UUID uuid = (UUID) context.getSessionData("uuid");
            return MarketLang.ENTER_AN_AMOUNT.get(Bukkit.getPlayer(uuid));
      }
}
