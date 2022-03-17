package net.pixlies.core.modules;

import lombok.Getter;
import net.pixlies.core.Main;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

public abstract class Module {

    @Getter private ModuleDescription description;
    @Getter private File moduleFolder;
    @Getter private Logger logger;

    public void init(ModuleDescription description) {
        this.description = description;
        moduleFolder = new File(Main.getInstance().getDataFolder().getAbsolutePath() + File.separator + "modules" + File.separator + description.getName());
        logger = Logger.getLogger(description.getName());
    }

    public InputStream getResource(@NotNull String string) {
        try {
            URL url = this.getClass().getClassLoader().getResource(string);

            if (url == null) {
                return null;
            } else {
                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            }

        } catch (IOException e) {
            return null;
        }
    }

    public void onLoad() {

    }

    public void onDrop() {

    }

}
