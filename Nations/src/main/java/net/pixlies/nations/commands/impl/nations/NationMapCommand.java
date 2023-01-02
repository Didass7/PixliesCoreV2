package net.pixlies.nations.commands.impl.nations;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.pixlies.nations.Nations;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.chunk.NationChunk;
import net.pixlies.nations.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.relations.Relation;
import net.pixlies.nations.utils.NationUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@CommandAlias("%nationscommand")
@Subcommand("map")
public class NationMapCommand extends BaseCommand {

    private static final Nations instance = Nations.getInstance();

    // -------------------------------------------------------------------------------------------------
    //                                         /n map chat
    // -------------------------------------------------------------------------------------------------
    @Subcommand("chat")
    @Description("Display nearby chunks and their occupation in chat")
    public void onChat(Player player) {
        renderChatMap(player);
    }

    public void renderChatMap(Player player) {
        // NationProfile is never null, check if the player is in a nation
        final long start = System.currentTimeMillis();
        NationProfile profile = NationProfile.get(player.getUniqueId());
        List<TextComponent> rows = new ArrayList<>();
        final int height = 6;
        final int width = (height * 2);

        final int playerChunkX = player.getLocation().getChunk().getX();
        final int playerChunkZ = player.getLocation().getChunk().getZ();
        final World world = player.getWorld();

        // for each row
        for (int row = height; row >= -height; row--) {
            TextComponent.Builder comp = Component.text();
            // for each column
            for (int x = width; x >= -width; x--) {

                final int chunkX = playerChunkX - x;
                final int chunkZ = playerChunkZ - row;
                boolean playerChunk = chunkX == playerChunkX && chunkZ == playerChunkZ;

                NationChunk nChunk = NationChunk.getClaimAt(world.getName(), chunkX, chunkZ);
                if (nChunk == null) {
                    TextComponent cComp = Component.text()
                            .content(playerChunk ? "§7█" : "§2█")
                            .hoverEvent(HoverEvent.showText(Component.text()
                                    .content("§cWilderness\n§7You may claim here.")
                                    .build()))
                            .build();
                    comp.append(cComp);

                    continue;
                }

                Nation nation = nChunk.getNation();
                if (nation == null) {
                    TextComponent cComp = Component.text()
                            .content(playerChunk ? "§7█" : "§2█")
                            .hoverEvent(HoverEvent.showText(Component.text()
                                    .content("§cWilderness\n§7You may claim here.")
                                    .build()))
                            .build();
                    comp.append(cComp);

                    continue;
                }

                String colorNationName = NationUtils.getColorNameFromNationChunk(player, nChunk);
                String colorNationDesc = NationUtils.getColorDescFromNationChunk(player, nChunk);

                String relationColor = NationUtils.getRelationColorFromNationChunk(player, nChunk);

                TextComponent cComp = Component.text()
                        .content(playerChunk ? "§7█" : relationColor + "█")
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .content(colorNationName + "\n" + NationsLang.NATION_SUBTITLE_COLOUR.get() + colorNationDesc)
                                .build()))
                        .build();
                comp.append(cComp);

            }
            rows.add(comp.build());
        }

        for (TextComponent r : rows)
            player.sendMessage(r);
        player.sendMessage("§7Legend: §a█Nation §8| §b█Neutral §8| §d█Ally §8| §2█Wilderness");
    }

}
