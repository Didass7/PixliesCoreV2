package net.pixlies.proxy.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import net.md_5.bungee.api.CommandSender;

@CommandAlias("maintenance")
@CommandPermission("pixlies.maintenance")
public class MaintenanceCommand extends BaseCommand {

    @Subcommand("add")
    @CommandCompletion("@players")
    public void onAdd(CommandSender sender) {
        // TODO
    }

    @Subcommand("remove")
    @CommandCompletion("@players")
    public void onRemove(CommandSender sender) {
        // TODO
    }

    @Subcommand("list")
    @CommandCompletion("@players")
    public void onList(CommandSender sender) {
        // TODO
    }

}
