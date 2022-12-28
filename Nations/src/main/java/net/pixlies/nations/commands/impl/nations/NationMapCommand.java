package net.pixlies.nations.commands.impl.nations;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.pixlies.nations.Nations;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.chunk.NationChunk;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("%nationscommand")
@Subcommand("map")
public class NationMapCommand extends BaseCommand {

    private static final Nations instance = Nations.getInstance();

    // -------------------------------------------------------------------------------------------------
    //                                         /n map chat
    // -------------------------------------------------------------------------------------------------
    @Subcommand("chat")
    @Description("Display nearby chunks and their occupation in chat")
    public void onChat(CommandSender sender) {
        renderChatMap((Player) sender);
    }

    public void renderChatMap(Player player) {
        final long start = System.currentTimeMillis();
        NationProfile profile = NationProfile.get(player.getUniqueId());
        List<TextComponent> rows = new ArrayList<>();
        final int height = 6;
        final int width = (height * 2);

        final int playerCX = player.getLocation().getChunk().getX();
        final int playerCZ = player.getLocation().getChunk().getZ();
        final World world = player.getWorld();
        for (int row = height; row >= -height; row--) {
            TextComponent.Builder comp = Component.text();
            for (int x = width; x >= -width; x--) {
                final int chunkX = playerCX - x,
                        chunkZ = playerCZ - row;
                NationChunk nc = NationChunk.getClaimAt(world.getName(), chunkX, chunkZ);
                if (chunkX == playerCX && chunkZ == playerCZ) {
                    if (nc == null) {
                        TextComponent cComp = Component.text().content("§2█").hoverEvent(HoverEvent.showText(Component.text().content("§cWilderness\n§7You may claim here.").build())).build();
                        comp.append(cComp);
                    } else {
                        Nation nation = nc.getNation();
                        TextComponent cComp = Component.text().content("§e█").hoverEvent(HoverEvent.showText(Component.text().content("§f" + nation.getName() + "\n" + "§7" + nation.getDescription()).build())).build();
                        comp.append(cComp);
                    }
                } else {
                    if (profile.isInNation()) {
                        if (nc == null) {
                            TextComponent cComp = Component.text().content("§2█").hoverEvent(HoverEvent.showText(Component.text().content("§cWilderness\n§7You may claim here.").build())).build();
                            comp.append(cComp);
                        } else {
                            String colChar = profile.getNation().getRelationTo(nc.getNation()).getColor();
                            Nation nation = nc.getNation();
                            TextComponent cComp = Component.text().content(colChar + "█").hoverEvent(HoverEvent.showText(Component.text().content(colChar + nation.getName() + "\n" + "§7" + nation.getDescription()).build())).build();
                            comp.append(cComp);
                        }
                    } else {
                        if (nc == null) {
                            TextComponent cComp = Component.text().content("§2█").hoverEvent(HoverEvent.showText(Component.text().content("§cWilderness\n§7You may claim here.").build())).build();
                            comp.append(cComp);
                        } else {
                            Nation nation = nc.getNation();
                            TextComponent cComp = Component.text().content("§e█").hoverEvent(HoverEvent.showText(Component.text().content("§f" + nation.getName() + "\n" + "§7" + nation.getDescription()).build())).build();
                            comp.append(cComp);
                        }
                    }
                }
            }
            rows.add(comp.build());
        }
        for (TextComponent r : rows)
            player.sendMessage(r);
        player.sendMessage("§7Legend: §b█You §8| §b█Yours §8| §d█Ally §8| §2█Wilderness");
    }

}
