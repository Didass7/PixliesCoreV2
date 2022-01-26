package net.pixlies.core.modules;

import com.google.gson.Gson;
import lombok.Data;
import lombok.SneakyThrows;
import net.pixlies.core.Main;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Data
public class ModuleManager {

    private static final Main instance = Main.getInstance();

    private final Map<Module, ModuleDescription> modules = new HashMap<>();

    @SneakyThrows
    public void loadModules() {
        File modulesFolder = new File(instance.getDataFolder().getAbsolutePath() + "/modules/");
        modulesFolder.mkdirs();
        if (!modulesFolder.exists())
            modulesFolder.mkdir();

        for (File jarFile : Objects.requireNonNull(modulesFolder.listFiles())) {
            if (!jarFile.getName().endsWith(".jar")) continue;
            Validate.notNull(jarFile, "File cannot be null");

            JarFile jar = null;
            InputStream stream = null;

            try {
                jar = new JarFile(jarFile);
                JarEntry entry = jar.getJarEntry("info.json");

                if (entry == null) {
                    instance.getLogger().severe("Module " + jarFile.getName() + " does not contain info.json! Contact the developer to fix this issue ASAP.");
                    return;
                }

                stream = jar.getInputStream(entry);

                ModuleDescription infoJson = new Gson().fromJson(IOUtils.toString(stream, StandardCharsets.UTF_8), ModuleDescription.class);

                if (modules.containsValue(infoJson)) {
                    instance.getLogger().warning("Module " + infoJson.getName() + " v" + infoJson.getVersion() + " has already been loaded!");
                    return;
                }

                URLClassLoader child = new URLClassLoader(
                        new URL[] {jarFile.toURI().toURL()},
                        this.getClass().getClassLoader()
                );
                Class<? extends Module> mainClass = Class.forName(infoJson.getMain(), true, child).asSubclass(Module.class);
                Module moduleInstance = mainClass.getDeclaredConstructor().newInstance();

                modules.put(moduleInstance, infoJson);

                instance.getLogger().info("Loading module " + infoJson.getName() + " v" + infoJson.getVersion() + "...");

                moduleInstance.init(infoJson);

                try {
                    moduleInstance.onLoad();
                    instance.getLogger().info("The module " + infoJson.getName() + " v" + infoJson.getVersion() + " has successfully loaded!");

                } catch (Exception e) {
                    e.printStackTrace();
                    instance.getLogger().severe("Failed to load module " + infoJson.getName() + " v" + infoJson.getVersion() + "!");
                }

            } finally {
                if (jar != null) {
                    try {
                        jar.close();
                    } catch (IOException ignored) {
                    }
                }
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }

    }

    public void unloadModules() {
        if (modules.isEmpty()) return;
        for (Map.Entry<Module, ModuleDescription> entry : modules.entrySet()) {
            Module module = entry.getKey();
            ModuleDescription description = entry.getValue();
            try {
                module.onDrop();
                instance.getLogger().info("The module " + description.getName() + " v" + description.getVersion() + " has successfully unloaded!");
            } catch (Exception e) {
                instance.getLogger().severe("Module " + description.getName() + " v" + description.getVersion() + " encountered an error while unloading.");
                e.printStackTrace();
            }
        }
    }

    public ModuleDescription getDescription(Module module) {
        if (modules.isEmpty()) return null;
        return modules.get(module);
    }

    public Module getModule(String string) {
        if (modules.isEmpty()) return null;
        for (Map.Entry<Module, ModuleDescription> entry : modules.entrySet()) {
            Module module = entry.getKey();
            ModuleDescription description = entry.getValue();
            if (!description.getName().equalsIgnoreCase(string)) continue;
            return module;
        }
        return null;
    }

    public static boolean activateModule(Module module) {
        if (module.getDescription().isActivated()) return false;
        try {
            module.onLoad();
        } catch (Exception e) {
            e.printStackTrace();
            instance.getLogger().severe("Module " + module.getDescription().getName() + " v" + module.getDescription().getVersion() + " encountered an error on activation.");
            return false;
        }
        module.getDescription().setActivated(true);
        return true;
    }

    public static boolean deactivateModule(Module module) {
        if (!module.getDescription().isActivated()) return false;
        try {
            module.onDrop();
        } catch (Exception e) {
            e.printStackTrace();
            instance.getLogger().severe("Module " + module.getDescription().getName() + " v" + module.getDescription().getVersion() + " encountered an error on deactivation.");
            return false;
        }
        module.getDescription().setActivated(false);
        return true;
    }

}
