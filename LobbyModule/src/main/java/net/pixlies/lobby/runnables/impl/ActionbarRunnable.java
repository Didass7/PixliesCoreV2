package net.pixlies.lobby.runnables.impl;

import net.kyori.adventure.text.Component;
import net.pixlies.core.runnables.PixliesRunnable;
import net.pixlies.core.utils.CC;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.managers.QueueManager;
import net.pixlies.lobby.managers.queue.Queue;
import net.pixlies.lobby.managers.queue.QueuePlayer;
import org.bukkit.entity.Player;

public class ActionbarRunnable extends PixliesRunnable {

    private static final Lobby instance = Lobby.getInstance();
    private static final QueueManager manager = instance.getQueueManager();

    public ActionbarRunnable() {
        super(true, 10, 10);
    }

    @Override
    public void run() {
        for (Player player : instance.getServer().getOnlinePlayers()) {
            if (!manager.isInQueue(player.getUniqueId())) continue;

            Queue queue = manager.getQueueFromPlayer(player.getUniqueId());
            if (queue == null) continue;

            QueuePlayer queuePlayer = queue.getQueuePlayer(player.getUniqueId());
            if (queuePlayer == null) continue;

            player.sendActionBar(Component.text(CC.format("&8In queue for &2" + queue.getName() + "&8: Position &5" + queuePlayer.getPosition() + "&8 out of &5" + queue.getSize() + "&8 players" + (queue.isPaused() ? " &8(&4Paused&8)" : ""))));

        }
    }

}
