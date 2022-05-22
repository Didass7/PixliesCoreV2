package net.pixlies.business.market;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

import java.time.Duration;

@AllArgsConstructor
public enum Challenge {

    BUY_ORDER(Lang.BUY_ORDER, 100),
    SELL_ORDER(Lang.SELL_ORDER, 100),
    BUY_ORDER_UNIT_50(Lang.BUY_ORDER_UNIT_50, 1000),
    BUY_ORDER_64_ITEMS(Lang.BUY_ORDER_64_ITEMS, 1000),
    BUY_ORDER_500_ITEMS(Lang.BUY_ORDER_500_ITEMS, 2000),

    SOLD_100_ITEMS(Lang.SOLD_100_ITEMS, 3000),
    GAINED_350_COINS(Lang.GAINED_350_COINS, 3000),
    ORDERED_ALL_ITEMS(Lang.ORDERED_ALL_ITEMS, 20000);

    private final Lang message;
    private final @Getter int xpGained;

    public String getMessage(Player player) {
        return message.get(player);
    }

    public void complete(Player player) {
        // TITLE
        TextComponent titleText = Component.text("Challenge complete!", NamedTextColor.GREEN, TextDecoration.BOLD);
        TextComponent subtitleText = Component.text("Received ", NamedTextColor.GRAY)
                .append(Component.text(xpGained, NamedTextColor.AQUA))
                .append(Component.text(" experience", NamedTextColor.GRAY));
        Title.Times times = Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(6000), Duration.ofMillis(1000));
        Title title = Title.title(titleText, subtitleText, times);
        player.showTitle(title);

        // BOSSBAR
        TextComponent bossbarText = Component.text(getMessage(player) + " ", NamedTextColor.WHITE)
                .append(Component.text("[", NamedTextColor.GRAY))
                .append(Component.text("✔", NamedTextColor.GREEN, TextDecoration.BOLD))
                .append(Component.text("]", NamedTextColor.GRAY));
        BossBar bossbar = BossBar.bossBar(bossbarText, 1, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);
        player.showBossBar(bossbar);

        // OTHER
        player.giveExp(xpGained);
        player.playSound(player.getLocation(), "ui.toast.challenge_complete", 50, 1);
        Lang.CHALLENGE_COMPLETED.send(player, "%CHALLENGE%;" + getMessage(player));

        User.get(player.getUniqueId()).getCompletedChallenges().add(this);
    }

}
