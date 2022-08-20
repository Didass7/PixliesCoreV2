package net.pixlies.core.runnables;

import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.core.runnables.impl.ActionBarRunnable;
import net.pixlies.core.runnables.impl.TabListRunnable;

public class RunnableRegisterManager {

    private final ImmutableList<PixliesRunnable> runnables = ImmutableList.of(
            new ActionBarRunnable(),
            new TabListRunnable()
    );

    public void runAll() {
        runnables.forEach(runnable -> Main.getInstance().getRunnableManager().register(runnable));
    }

    public void stopAll() {
        runnables.forEach(runnable -> Main.getInstance().getRunnableManager().unregister(runnable));
    }

}
