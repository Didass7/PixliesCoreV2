package net.pixlies.business.handlers;

import com.google.common.collect.ImmutableList;
import net.pixlies.business.ProtoBusinesss;

import java.util.HashMap;
import java.util.Map;

public class HandlerManager {
    private static final ProtoBusinesss instance = ProtoBusinesss.getInstance();
    
    private final Map<Class<? extends Handler>, Handler> handlers = new HashMap<>();
    private final ImmutableList<Class<? extends Handler>> handlerList = ImmutableList.of();
    
    public void registerAllHandlers() {
        handlerList.forEach(this::register);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Handler> T getHandler(Class<T> clazz) {
        if (!handlers.containsKey(clazz)) {
            try {
                T handler = clazz.getDeclaredConstructor().newInstance();
                handlers.put(clazz, handler);
                return handler;
            } catch (Exception e) {
                e.printStackTrace();
                instance.getLogger().severe("Failed to register handler.");
            }
        }
        return (T) handlers.get(clazz);
    }
    
    public void register(Class<? extends Handler> clazz) {
        if (handlers.containsKey(clazz)) return;
        try {
            clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            instance.getLogger().severe("Failed to register handler.");
        }
    }
}
