package net.pixlies.core.utils;

import net.pixlies.core.Main;

import java.io.File;

public final class FileUtils {

    private FileUtils() {}

    public static void saveIfNotExists(String local) {
        File file = new File(Main.getInstance().getDataFolder().getAbsolutePath(), local);
        if (!file.exists()) {
            Main.getInstance().saveResource(local, false);
        }
    }

}
