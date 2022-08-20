package net.pixlies.core.packets;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.google.common.collect.ImmutableList;
import net.pixlies.core.packets.impl.player.TabListPacketAdapter;

public class PacketAdapterManager {

    private final ImmutableList<PacketAdapter> adapters = ImmutableList.of(
            new TabListPacketAdapter()
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
