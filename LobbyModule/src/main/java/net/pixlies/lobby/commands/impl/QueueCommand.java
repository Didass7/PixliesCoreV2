package net.pixlies.lobby.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.managers.QueueManager;
import org.bukkit.entity.Player;

public class QueueCommand extends BaseCommand {

    private static final QueueManager manager = Lobby.getInstance().getQueueManager();

    @CommandAlias("queue|play|join|joinserver|joinqueue")
    @CommandCompletion("@empty")
    @Syntax("<server>")
    public void onQueue(Player player, @Single String server) {

        manager.addPlayerToQueue(player, server);

    }

}


