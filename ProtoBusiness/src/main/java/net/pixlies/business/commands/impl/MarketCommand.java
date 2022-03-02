package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

@CommandAlias("market|m|nasdaq|nyse|snp500")
@CommandPermission("pixlies.business.market")
public class MarketCommand extends BaseCommand {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);

    @Default
    @Description("Opens the market menu")
    public void onMarket(Player player) {
        // TODO: open the market menu
        // TODO: stats + personal stats in market menu
    }

    @Subcommand("open")
    @CommandPermission("pixlies.business.market.gates")
    @Description("Opens the market to the public")
    public void onMarketOpen(Player player) {
        if (marketHandler.isMarketOpen()) Lang.MARKET_WAS_ALREADY_OPEN.send(player);
        else {
            marketHandler.setMarketOpen(true);
            Lang.MARKET_OPEN.broadcast();
        }
    }

    @Subcommand("close")
    @CommandPermission("pixlies.business.market.gates")
    @Description("Closes the market")
    public void onMarketClose(Player player) {
        if (!marketHandler.isMarketOpen()) Lang.MARKET_WAS_ALREADY_CLOSED.send(player);
        else {
            marketHandler.setMarketOpen(false);
            Lang.MARKET_CLOSED.broadcast();
        }
    }

    @Subcommand("reset")
    @CommandPermission("pixlies.business.market.reset")
    @Description("Resets the market statistics")
    public void onMarketReset(Player player) {
        // TODO
    }


    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
