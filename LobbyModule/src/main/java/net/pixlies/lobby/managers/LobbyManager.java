package net.pixlies.lobby.managers;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LobbyManager {

    @Getter List<UUID> buildModePlayers = new ArrayList<>();

}
