package net.pixlies.proxy.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public final class CC {

    private CC() {}

    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static BaseComponent asComponent(String string) {
        return new TextComponent(format(string));
    }

}
