package net.pixlies.core.commands.debug;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.pixlies.core.Main;
import net.pixlies.core.modules.ModuleDescription;
import net.pixlies.core.modules.Module;
import net.pixlies.core.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@CommandAlias("modules")
@CommandPermission("pixlies.debug.modules")
public class ModulesCommand extends BaseCommand {

    @Default
    @CommandCompletion("")
    @Description("Returns all loaded Modules")
    public void onModules(CommandSender commandSender) {
        // commandSender.sendMessage("PixliesCoreV2 Modules: " + getModuleNames());

        ChestGui gui = new ChestGui(3, "§aModules");
        PaginatedPane modulesPane = new PaginatedPane(0, 0, 9, 5);

        modulesPane.populateWithGuiItems(new ArrayList<>(){{
            for(Map.Entry<Module, ModuleDescription> entry : modules.entrySet()) {
                Material icon = entry.getValue().getIcon();
                StringJoiner authorJoiner = new StringJoiner("§7, ");
                for (String author : entry.getValue().getAuthors())
                    authorJoiner.add("§b" + author);
                ChatColor nameColor = entry.getValue().isActivated() ? ChatColor.GREEN : ChatColor.RED;
                ItemBuilder builder = new ItemBuilder(icon)
                        .setDisplayName(nameColor + entry.getValue().getName() + " v" + entry.getValue().getVersion())
                        .addLoreLine("§7by " + authorJoiner.toString());

                add(new GuiItem(builder.build(), e -> e.setCancelled(true)));
            }
        }});

        gui.addPane(modulesPane);

        gui.show((Player) commandSender);
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    public ModulesCommand() { }

    private final Map<Module, ModuleDescription> modules = Main.getInstance().getModuleManager().getModules();

    private List<String> getModuleNames() {
        List<String> moduleNames = new ArrayList<>();
        for(Map.Entry<Module, ModuleDescription> entry : modules.entrySet()) {
            moduleNames.add(entry.getValue().getName());
        }
        return moduleNames;
    }

}
