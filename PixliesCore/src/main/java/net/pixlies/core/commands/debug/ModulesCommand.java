package net.pixlies.core.commands.debug;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.pixlies.core.Main;
import net.pixlies.core.modules.ModuleDescription;
import net.pixlies.core.modules.Module;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CommandAlias("modules")
@CommandPermission("pixlies.debug.modules")
public class ModulesCommand extends BaseCommand {

    @Default
    @CommandCompletion("")
    @Description("Returns all loaded Modules")
    public void onModules(CommandSender commandSender) {
        commandSender.sendMessage("PixliesCoreV2 Modules: " + getModuleNames());

        ChestGui gui = new ChestGui(3, "§aModules");
        PaginatedPane modulesPane = new PaginatedPane(0, 0, 9, 5);

        modulesPane.populateWithGuiItems(new ArrayList<>(){{
            for(Map.Entry<Module, ModuleDescription> entry : modules.entrySet()) {
                Material icon = entry.getValue().getIcon();

            }
        }});

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
