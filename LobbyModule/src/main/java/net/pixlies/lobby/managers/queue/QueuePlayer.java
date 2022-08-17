package net.pixlies.lobby.managers.queue;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class QueuePlayer {

    private final UUID uuid;
    private int position;

}
