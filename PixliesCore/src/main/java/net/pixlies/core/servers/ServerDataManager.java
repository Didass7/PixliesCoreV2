package net.pixlies.core.servers;

import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ServerDataManager {

    private static final Main instance = Main.getInstance();
    private static final Config config = instance.getConfig();

    @Getter private final String serverName = config.getString("server.name", instance.getServer().getName());
    @Getter private final String displayName = config.getString("server.displayName", serverName);

    @Getter private final Map<String, ServerData> servers = new HashMap<>();

    public ServerData getThisServerData() {
        return new ServerData(
                serverName,
                displayName,
                instance.getServer().getTPS()[0],
                !instance.getServer().hasWhitelist(),
                instance.getServer().getOnlinePlayers().size(),
                System.currentTimeMillis()
        );
    }

    public @Nullable ServerData getByName(String serverName) {
        return servers.get(serverName);
    }

    public @Nullable ServerData getByDisplayName(String displayName) {
        return servers.values().stream().filter(serverData -> serverData.getDisplayName().equals(displayName)).findFirst().orElse(null);
    }

}
