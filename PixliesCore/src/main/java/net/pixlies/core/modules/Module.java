package net.pixlies.core.modules;

import lombok.Getter;
import net.pixlies.core.Main;

import java.io.File;
import java.io.InputStream;
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

    public InputStream getResource(String string) {
        return this.getClass().getClassLoader().getResourceAsStream(string);
    }

    public abstract void onLoad();

    public abstract void onDrop();

}
