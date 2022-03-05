package net.pixlies.core.runnables.impl;

import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.runnables.PixliesRunnable;

import java.util.Collection;

public class UserRunnable extends PixliesRunnable {

    public UserRunnable() {
        super(true, 6000, 6000);
    }

    @Override
    public void run() {
        Collection<User> users = Main.getInstance().getDatabase().getUserCache().values();
        Main.getInstance().getLogger().info("Backing up all users...");
        users.forEach(User::backup);
        Main.getInstance().getLogger().info("All users have been backed up!");
    }

}
