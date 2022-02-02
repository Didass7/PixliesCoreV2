package net.pixlies.core.commands.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import net.pixlies.core.Main;
import net.pixlies.core.modules.ModuleDescription;
import net.pixlies.core.modules.Module;
import net.pixlies.core.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

@CommandAlias("modules")
@CommandPermission("pixlies.admin.modules")
public class ModulesCommand extends BaseCommand {

    private final Map<Module, ModuleDescription> modules = Main.getInstance().getModuleManager().getModules();

    @Default
    @Description("Returns all loaded modules")
    public void onModules(Player player) {
        ChestGui gui = new ChestGui(3, "Modules");
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
                        .addLoreLine("§7by " + authorJoiner);

                add(new GuiItem(builder.build(), e -> e.setCancelled(true)));
            }
        }});

        gui.addPane(modulesPane);
        gui.show(player);
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

}
