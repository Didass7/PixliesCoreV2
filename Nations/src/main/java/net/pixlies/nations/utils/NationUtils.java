package net.pixlies.nations.utils;

import java.util.Random;

/**
 * Different utilities for nations
 *
 * @author MickMMars
 */
public class NationUtils {

    public static String randomDescription() {
        String[] arr = new String[] {
            "No description yet",
            "Use /n description to change me!",
            "Pixlies has a place for you",
            "Be creative!"
        };

        Random r = new Random();
        int randomNumber = r.nextInt(arr.length);

        return arr[randomNumber];
    }

    public static boolean nameValid(String name) {
        if (name == null) return false;
        if (name.length() > 16 || name.length() < 4) return false;
        if (!name.matches("^[a-zA-Z0-9_-]*$")) return false;
        return !name.equalsIgnoreCase("confirm");
    }

}
