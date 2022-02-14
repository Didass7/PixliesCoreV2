package net.pixlies.proxy.handlers.impl;

import net.md_5.bungee.config.Configuration;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.entity.Queue;
import net.pixlies.proxy.handlers.Handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueueHandler implements Handler {

    private static final Proxy instance = Proxy.getInstance();
    private final Config config = instance.getConfig();

    private final List<Queue> queues = new ArrayList<>();

    public QueueHandler() {
        init();
    }

    public Queue getQueue(String server) {
        for (Queue queue : queues) {
            if (server.equalsIgnoreCase(queue.getServerName())) {
                return queue;
            }
        }
        return null;
    }

    public void init() {

        if (!config.getConfig().getBoolean("queue.enabled", true)) return;
        Configuration section = config.getConfig().getSection("queue.queues");
        if (section == null) return;

        for (String queueName : section.getKeys()) {
            if (instance.getProxy().getServerInfo(queueName) == null) {
                instance.getLogger().warning("The server \"" + queueName + "\" does not exist.");
                continue;
            }
            Configuration queueSection = section.getSection("queue.queues." + queueName);
            if (queueSection == null) continue;
            queues.add(new Queue(
                    queueName,
                    queueSection.getString("permission", ""),
                    queueSection.getInt("max-players", 20)
            ));
        }

    }

    public Collection<Queue> getQueues() {
        queues.removeIf(queue -> queue.getServerInfo() == null);
        return queues;
    }

}
