package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import net.pixlies.core.Main;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("kickall")
@CommandPermission("pixlies.moderation.kickall")
public class KickallCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();

    @Default
    @Description("Kick all players")
    @CommandCompletion("@empty")
    public void onKickAll(CommandSender sender) {
        sender.sendMessage(CC.format(Lang.PIXLIES + "&7Are you sure you want to kick all players? Type \"&c/kickall confirm [reason]\"&7 to confirm."));
    }

    @Private
    @Subcommand("confirm")
    @Syntax("[reason]")
    public void onConfirm(CommandSender sender, @Optional String reason) {
        String newReason = reason == null ? instance.getConfig().getString("moderation.defaultReason", "No reason given") : reason;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("pixlies.moderation.kick.exempt")) continue;
            player.kick(Component.text(Lang.KICK_MESSAGE.get()
                    .replace("%REASON%", newReason)
            ));
        }

        Lang.MODERATION_ISSUED_KICKALL.broadcast("%PLAYER%;" + sender.getName(), "%REASON%;" + reason);
    }

}
