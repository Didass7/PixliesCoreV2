package net.pixlies.business.threads;

import net.pixlies.business.market.Embargo;

import java.util.Map;

/**
 * Checks for embargo expirations.
 *
 * @author vyketype
 */
public class EmbargoExpirationThread extends Thread {
    public static Map<String, Integer> EMBARGO_EXPIRATIONS;
    
    public boolean running = false;
    
    // Runs every minute
    public void run() {
        while (running) {
            try {
                tick();
                sleep(1000 * 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void tick() {
        for (Map.Entry<String, Integer> entry : EMBARGO_EXPIRATIONS.entrySet()) {
            if (entry.getValue() != 0) {
                entry.setValue(entry.getValue() - 1);
                continue;
            }
    
            Embargo embargo = Embargo.get(entry.getKey());
            embargo.delete();
            embargo.sendDeletionMessages();
            EMBARGO_EXPIRATIONS.remove(entry.getKey());
        }
    }
    
    public void startThread() {
        this.running = true;
        this.start();
    }
    
    public void stopThread() {
        this.running = false;
    }
}
