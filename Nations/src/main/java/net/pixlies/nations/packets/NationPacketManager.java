package net.pixlies.nations.packets;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.google.common.collect.ImmutableList;

public class NationPacketManager {

    private final ImmutableList<PacketAdapter> adapters = ImmutableList.of(
    );

    public void registerAll() {
        for (PacketAdapter adapter : adapters) {
            ProtocolLibrary.getProtocolManager().addPacketListener(adapter);
        }
    }

    public void unregisterAll() {
        for (PacketAdapter adapter : adapters) {
            ProtocolLibrary.getProtocolManager().removePacketListener(adapter);
        }
    }

}
