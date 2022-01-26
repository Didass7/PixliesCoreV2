package net.pixlies.nations.menus;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MenuButton {

    @Getter private final List<ClickButton> clickButtons = new ArrayList<>();
    @Getter private final ItemStack item;

    public MenuButton(ItemStack item) {
        this.item = item;
    }

    public MenuButton addEvent(ClickButton clickButton) {
        clickButtons.add(clickButton);
        return this;
    }

}
