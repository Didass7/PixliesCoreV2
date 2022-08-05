package net.pixlies.proxy.utils;

import net.pixlies.proxy.PixliesProxy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Make file stuff easier
 * @author Dynmie
 */
public final class FileUtils {

    private FileUtils() {}

    private static final PixliesProxy instance = PixliesProxy.getInstance();

    public static void saveResource(String path, boolean replace) {

        File file = new File(instance.getDataFolder() + "/" + path);

        file.getParentFile().mkdirs();

        if (!replace && file.exists()) return;

        if (file.exists()) {
            file.delete();
        }

        try (InputStream stream = instance.getResourceAsStream(path)) {
            file.createNewFile();
            Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
