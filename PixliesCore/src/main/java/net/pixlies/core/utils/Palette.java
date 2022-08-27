package net.pixlies.core.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.ChatColor;

@Data
@AllArgsConstructor
public class Palette {

    public static final Palette GREEN_THICK = new Palette(ChatColor.BOLD, ChatColor.GREEN, ChatColor.DARK_GREEN);

    private ChatColor effect;
    private ChatColor primary;
    private ChatColor accent;

}
