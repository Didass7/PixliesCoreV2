package net.pixlies.core.servers;

import com.google.gson.Gson;
import net.pixlies.core.Main;
import net.pixlies.core.database.redis.RedisManager;
import net.pixlies.core.runnables.PixliesRunnable;
import net.pixlies.core.utils.json.JsonBuilder;

public class ServerDataRunnable extends PixliesRunnable {

    public ServerDataRunnable() {
        super(false, 400, 100); // 20s, 5s
    }

    @Override
    public void run() {

        ServerData data = Main.getInstance().getServerDataManager().getThisServerData();

        RedisManager.sendRequest("ServerData", new JsonBuilder()
                .add("data", new Gson().toJsonTree(data))
                .toJsonObject());

    }

}
