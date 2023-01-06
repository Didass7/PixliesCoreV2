package net.pixlies.business.market.conversations;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.profiles.OrderProfile;
import net.pixlies.business.util.SoundUtil;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class AmountPrompt extends NumericPrompt {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      
      @Override
      protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull Number input) {
            UUID uuid = (UUID) context.getSessionData("uuid");
      
            if (input.doubleValue() % 1 != 0 || input.intValue() < 0) {
                  MarketLang.THIS_IS_NOT_A_POSITIVE_INTEGER.get(Bukkit.getPlayer(uuid));
                  SoundUtil.cancelledOrder(Bukkit.getPlayer(uuid));
                  OrderProfile.get(uuid).remove();
                  return Prompt.END_OF_CONVERSATION;
            }
            
            OrderProfile profile = OrderProfile.get(uuid);
            profile.getTempOrder().setAmount(input.intValue());
            profile.save();
            // TODO: open new GUI
            return Prompt.END_OF_CONVERSATION;
      }
      
      @Override
      public @NotNull String getPromptText(@NotNull ConversationContext context) {
            UUID uuid = (UUID) context.getSessionData("uuid");
            return MarketLang.ENTER_AN_AMOUNT.get(Bukkit.getPlayer(uuid));
      }
}
