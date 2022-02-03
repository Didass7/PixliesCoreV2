package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;

@CommandAlias("workbench|craft|wb")
@CommandPermission("pixlies.cosmetics.workbench")
public class WorkBenchCommand extends BaseCommand {

    @Default
    @Description("Craft something useful")
    public void onWorkBench(Player player) {
        player.openWorkbench(null, true);
    }

}
