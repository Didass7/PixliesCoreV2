package net.pixlies.lobby.runnables;

import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.core.runnables.PixliesRunnable;
import net.pixlies.lobby.runnables.impl.ActionbarRunnable;

import java.util.Collection;

public class RunnableManager {

    private final Collection<PixliesRunnable> runnables = ImmutableList.of(
            new ActionbarRunnable()
    );

    public void registerAll() {
        runnables.forEach(r -> Main.getInstance().getRunnableManager().register(r));
    }

    public void unregisterAll() {
        runnables.forEach(r -> Main.getInstance().getRunnableManager().unregister(r));
    }

}
