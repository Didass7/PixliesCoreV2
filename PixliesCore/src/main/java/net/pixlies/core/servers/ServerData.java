package net.pixlies.core.servers;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ServerData {

    private final String serverName;
    private String displayName;
    private double tps;
    private boolean joinable;
    private int playerCount;
    private long lastUpdate;

    public boolean isOnline() {
        return System.currentTimeMillis() - lastUpdate < 20000; // 20 seconds
    }

}
