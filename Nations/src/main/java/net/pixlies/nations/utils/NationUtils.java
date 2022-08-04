package net.pixlies.nations.utils;

import org.apache.commons.lang.StringUtils;

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
        if (!StringUtils.isAlphanumeric(name)) return false;
        if (name.equalsIgnoreCase("confirm")) return false;
        return name.length() <= 16 && name.matches("^[a-zA-Z0-9_-]*$");
    }

}
