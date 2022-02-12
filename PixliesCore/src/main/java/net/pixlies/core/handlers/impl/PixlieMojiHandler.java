package net.pixlies.core.handlers.impl;

import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.handlers.Handler;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * PixlieMoji
 * @author Dynmie
 * @author MickMMars
 */
public class PixlieMojiHandler implements Handler {

    private static final Main instance = Main.getInstance();
    private final Config config = instance.getConfig();

    private final HashMap<String, Character> pixlieMojis = new HashMap<>();

    public String replace(String message) {
        String toReturn = message;
        for (Map.Entry<String, Character> entries : pixlieMojis.entrySet())
            toReturn = toReturn.replace(entries.getKey(), entries.getValue().toString());
        return toReturn;
    }

    public void loadEmojis() {
        if (!pixlieMojis.isEmpty()) pixlieMojis.clear();
        ConfigurationSection section = config.getConfigurationSection("emojis");
        if (section == null) return;
        Set<String> keys = section.getKeys(false);
        keys.forEach(key -> {
            String emoji = config.getString("emojis." + key);
            if (emoji == null) return;
            pixlieMojis.put(":" + key + ":", emoji.charAt(0)); // :key:, emoji
        });
    }

    public boolean isEmoji(String string) {
        return pixlieMojis.containsKey(string);
    }

    public @Nullable Character getEmoji(String string) {
        return pixlieMojis.get(string);
    }

    public static String replaceEmojis(String emojis) {
        return instance.getHandlerManager().getHandler(PixlieMojiHandler.class).replace(emojis);
    }

}
