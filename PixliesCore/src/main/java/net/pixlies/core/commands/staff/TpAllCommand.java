package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.TeleportHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpAllCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final TeleportHandler tpHandler = instance.getHandlerManager().getHandler(TeleportHandler.class);

    @CommandAlias("tpall")
    @CommandPermission("pixlies.staff.tpall")
    @Description("Teleports all players to sender")
    public void onTpAll(CommandSender sender) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            tpHandler.setBackLocation(p.getUniqueId(), p.getLocation());
            p.teleport((Player) sender);
        }
        Lang.TPALL_BROADCAST.broadcast("%PLAYER%;" + sender.getName());
    }

}
