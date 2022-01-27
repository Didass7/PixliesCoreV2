package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVisionCommand extends BaseCommand {

    @Default
    @CommandCompletion("@empty")
    public void onNightVision(Player player) {
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            Lang.COSMETICS_NIGHTVISION_OFF.send(player);
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
            Lang.COSMETICS_NIGHTVISION_ON.send(player);
        }
    }

    @Private
    @CommandPermission("pixlies.cosmetics.nightvision.others")
    @CommandCompletion("@players")
    public void onNightVision(Player sender, Player target) {
        if (target.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            target.removePotionEffect(PotionEffectType.NIGHT_VISION);
            Lang.COSMETICS_NIGHTVISION_OFF.send(sender);
        } else {
            target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
            Lang.COSMETICS_NIGHTVISION_ON.send(sender);
        }
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
