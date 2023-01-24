package net.pixlies.business.market;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.pixlies.business.PixliesEconomy;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.util.SoundUtil;
import org.bukkit.entity.Player;

import java.time.Duration;

@AllArgsConstructor
public enum Challenge {
    BUY_ORDER(MarketLang.BUY_ORDER, 100),
    SELL_ORDER(MarketLang.SELL_ORDER, 100),
    BUY_ORDER_UNIT_50(MarketLang.BUY_ORDER_UNIT_50, 1000),
    BUY_ORDER_64_ITEMS(MarketLang.BUY_ORDER_64_ITEMS, 1000),
    BUY_ORDER_500_ITEMS(MarketLang.BUY_ORDER_500_ITEMS, 2000),
    
    SOLD_100_ITEMS(MarketLang.SOLD_100_ITEMS, 3000),
    GAINED_350_COINS(MarketLang.GAINED_350_COINS, 3000),
    ORDERED_ALL_ITEMS(MarketLang.ORDERED_ALL_ITEMS, 20000);
    
    private final MarketLang message;
    private final @Getter
    int xpGained;
    
    private static final PixliesEconomy instance = PixliesEconomy.getInstance();
    private static final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);
    
    public String getMessage(Player player) {
        return message.get(player);
    }
    
    // TODO: Move to MarketProfile
    public void complete(Player player) {
        // TITLE
        TextComponent titleText = Component.text("Challenge complete!", NamedTextColor.GREEN, TextDecoration.BOLD);
        TextComponent subtitleText = Component.text("Received ", NamedTextColor.GRAY)
                .append(Component.text(xpGained, NamedTextColor.AQUA))
                .append(Component.text(" experience", NamedTextColor.GRAY));
        Title.Times times = Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(6000),
                Duration.ofMillis(1000));
        Title title = Title.title(titleText, subtitleText, times);
        player.showTitle(title);
        
        // BOSSBAR
        TextComponent bossbarText = Component.text(getMessage(player) + " ", NamedTextColor.WHITE)
                .append(Component.text("[", NamedTextColor.DARK_GRAY))
                .append(Component.text("✔", NamedTextColor.GREEN, TextDecoration.BOLD))
                .append(Component.text("]", NamedTextColor.DARK_GRAY));
        BossBar bossbar = BossBar.bossBar(bossbarText, 1, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);
        player.showBossBar(bossbar);
        
        // OTHER
        player.giveExp(xpGained);
        SoundUtil.challengeComplete(player);
        MarketLang.CHALLENGE_COMPLETED.send(player, "%CHALLENGE%;" + getMessage(player));
        
        marketHandler.getChallenges().put(player.getUniqueId().toString(), this);
    }
}
