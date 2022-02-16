package net.pixlies.nations.runnables.impl;

import net.pixlies.core.Main;
import net.pixlies.core.runnables.PixliesRunnable;
import net.pixlies.nations.Nations;

public class SaveRunnable extends PixliesRunnable {

    private final Nations instance = Nations.getInstance();

    public SaveRunnable() {
        super(true, Main.getInstance().getConfig().getLong("save.period", 12000), Main.getInstance().getConfig().getLong("save.period", 12000));
    }

    @Override
    public void run() {
        instance.getLogger().info("Backing up all nations...");
        instance.getNationManager().backupAll();
        instance.getLogger().info("Backed up all nations!");
    }

}
