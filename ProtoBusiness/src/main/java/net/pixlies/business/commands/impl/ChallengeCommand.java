package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.Challenge;
import org.bukkit.entity.Player;

@CommandAlias("challenge|challenges")
@CommandPermission("pixlies.business.challenge")
public class ChallengeCommand extends BaseCommand {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);

    @Default
    @Description("Gets a list of all market challenges")
    public void onChallenge(Player player) {
        MarketLang.CHALLENGE_LIST.send(player);
        for (Challenge c : Challenge.values()) {
            if (marketHandler.getChallenges().containsEntry(player.getUniqueId().toString(), c)) {
                MarketLang.CHALLENGE_COMPLETE_FORMAT.send(player, "%CHALLENGE%;" + c.getMessage(player));
                return;
            }
            MarketLang.CHALLENGE_INCOMPLETE_FORMAT.send(player, "%CHALLENGE%;" + c.getMessage(player));
        }
    }

}
