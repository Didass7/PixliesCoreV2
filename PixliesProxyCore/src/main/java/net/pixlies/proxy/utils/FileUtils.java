package net.pixlies.proxy.utils;

import net.pixlies.proxy.Proxy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Make file stuff easier
 * @author Dynmie
 */
public final class FileUtils {

    private FileUtils() {}

    private static final Proxy instance = Proxy.getInstance();

    public static void saveResource(String path, boolean replace) {

        File file = new File(instance.getDataFolder(), path);

        if (!replace && file.exists()) return;

        if (replace) {
            file.delete();
        }

        try (InputStream stream = instance.getResourceAsStream(path)) {
            if (stream == null) return;
            Files.copy(stream, file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
