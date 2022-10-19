package net.pixlies.core.servers;

import com.google.gson.Gson;
import net.pixlies.core.Main;
import net.pixlies.core.database.redis.RedisMessageReceiveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ServerDataListener implements Listener {

    private static final Main instance = Main.getInstance();
    private static final ServerDataManager manager = instance.getServerDataManager();

    @EventHandler
    public void onReceive(RedisMessageReceiveEvent event) {
        if (!event.getIdentifier().equals("ServerData")) return;

        ServerData data = new Gson().fromJson(event.getData().get("data"), ServerData.class);
        synchronized (manager.getServers()) {
            manager.getServers().put(data.getServerName(), data);
        }

    }

}
