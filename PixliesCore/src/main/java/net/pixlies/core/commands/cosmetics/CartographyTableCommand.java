package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;

@CommandAlias("cartography|carto|carrot|cart")
@CommandPermission("pixlies.cosmetics.cartography")
public class CartographyTableCommand extends BaseCommand {

    @Default
    @Description("Repairs items")
    public void onCartography(Player player) {
        player.openCartographyTable(null, true);
    }

}
