package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

@CommandAlias("heal|hl")
@CommandPermission("pixlies.cosmetics.heal")
public class HealCommand extends BaseCommand {

    @Default
    @CommandCompletion("@empty")
    public void onHeal(Player player) {
        healPlayer(player);
        Lang.COSMETICS_HEALED_SELF.send(player);
    }

    @CommandPermission("pixlies.cosmetics.heal.others")
    @CommandCompletion("@players")
    public void onHeal(CommandSender sender, Player player) {
        healPlayer(player);
        Lang.COSMETICS_HEALED_OTHERS.send(sender, "%PLAYER%;" + player.getName());
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    private static void healPlayer(Player player) {
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue());
        player.setFoodLevel(20);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
    }

}
