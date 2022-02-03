package net.pixlies.core.runnables.impl;

import net.pixlies.core.entity.User;
import net.pixlies.core.runnables.PixliesRunnable;

import java.util.Collection;

public class UserRunnable extends PixliesRunnable {

    public UserRunnable() {
        super(true, 6000, 6000);
    }

    @Override
    public void run() {
        Collection<User> users = instance.getDatabase().getUserCache().values();
        instance.getLogger().info("Backing up all users...");
        users.forEach(User::backup);
        instance.getLogger().info("All users have been backed up!");
    }

}
