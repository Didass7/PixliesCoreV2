package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;

@CommandAlias("anvil|blacksmith")
@CommandPermission("pixlies.cosmetics.anvil")
public class AnvilCommand extends BaseCommand {

    @Default
    @Description("Open your favourite anvil")
    public void onAnvil(Player player) {
        player.openAnvil(null, true);
    }

}

