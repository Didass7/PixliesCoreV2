package net.pixlies.proxy.runnables;

import com.google.common.collect.ImmutableList;
import net.pixlies.proxy.runnables.impl.AutoAnnounceRunnable;
import net.pixlies.proxy.runnables.impl.QueueMessageRunnable;
import net.pixlies.proxy.runnables.impl.QueueRunnable;

import java.util.Collection;

public class RunnableManager {

    private final Collection<PixliesRunnable> runnables = ImmutableList.of(
            new AutoAnnounceRunnable(),
            new QueueRunnable(),
            new QueueMessageRunnable()
    );

    public void runAll() {
        runnables.forEach(PixliesRunnable::execute);
    }

}
