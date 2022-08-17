package net.pixlies.lobby.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Syntax;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.managers.QueueManager;
import org.bukkit.entity.Player;

public class LeaveQueueCommand extends BaseCommand {

    private static final QueueManager manager = Lobby.getInstance().getQueueManager();

    @CommandAlias("leavequeue|unplay")
    @CommandCompletion("@empty")
    @Syntax("<server>")
    public void onLeaveQueue(Player player) {

        manager.removePlayerFromQueue(player);

    }

}
