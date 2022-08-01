package net.pixlies.core.handlers.impl;

import lombok.Getter;
import net.pixlies.core.entity.user.timers.Timer;
import net.pixlies.core.handlers.Handler;

import java.util.*;

public class TimerHandler implements Handler {

   private final @Getter Map<String, Timer> allGlobalTimers = new HashMap<>();

    public Collection<Timer> getGlobalTimers() {
        if (allGlobalTimers.isEmpty()) return Collections.emptyList();
        for (Map.Entry<String, Timer> entry : allGlobalTimers.entrySet()) {
            String identifier = entry.getKey();
            Timer timer = entry.getValue();
            if (!timer.isExpired()) {
                continue;
            }
            allGlobalTimers.remove(identifier);
        }
        List<Timer> timers = new ArrayList<>(allGlobalTimers.values());
        timers.sort(Comparator.comparing(Timer::getDisplayName));
        return timers;
    }

    public boolean hasGlobalTimers() {
        return getGlobalTimers().size() > 0;
    }

    public boolean hasGlobalTimer(String identifier) {
        return allGlobalTimers.containsKey(identifier);
    }

    public void addGlobalTimer(Timer timer) {
        allGlobalTimers.put(timer.getUniqueId().toString(), timer);
    }

}
