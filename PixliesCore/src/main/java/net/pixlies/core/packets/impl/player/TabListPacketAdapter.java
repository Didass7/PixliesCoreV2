package net.pixlies.core.packets.impl.player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.clip.placeholderapi.libs.kyori.adventure.text.TextComponent;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.handlers.impl.TabListHandler;
import net.pixlies.core.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabListPacketAdapter extends PacketAdapter {

    private static final Main instance = Main.getInstance();
    private static final TabListHandler handler = instance.getHandlerManager().getHandler(TabListHandler.class);

    public TabListPacketAdapter() {
        super(instance, PacketType.Play.Server.PLAYER_INFO);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.getPacket().getPlayerInfoAction().read(0) != EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME) return;
        // The target to send the packet to
        Player target = event.getPlayer();

        List<PlayerInfoData> playerList = event.getPacket().getPlayerInfoDataLists().read(0); // 0 Action, 1 Number of Players, 0 PlayerInfoList

        // For each player info being sent to the client
        List<PlayerInfoData> updatedPlayerList = new ArrayList<>();
        for (PlayerInfoData playerInfo : playerList) {

            if (playerInfo == null || playerInfo.getProfile() == null) {
                updatedPlayerList.add(playerInfo);
                continue;
            }

            Player player = Bukkit.getPlayer(playerInfo.getProfile().getUUID());
            if (player == null) {
                updatedPlayerList.add(playerInfo);
                continue;
            }

            boolean isStaff = target.hasPermission("pixlies.staff");

            String prefix = handler.getTabListPrefix(player, isStaff);
            String suffix = handler.getTabListSuffix(player, isStaff);

            WrappedGameProfile profile = playerInfo.getProfile();
            PlayerInfoData newPlayerInfo = new PlayerInfoData(
                    profile,
                    playerInfo.getLatency(),
                    playerInfo.getGameMode(),
                    WrappedChatComponent.fromLegacyText(CC.format(prefix + player.getPlayerListName() + suffix))
            );

            updatedPlayerList.add(newPlayerInfo);
        }

        // Set the packet
        event.getPacket().getPlayerInfoDataLists().write(0, updatedPlayerList);
    }

}
