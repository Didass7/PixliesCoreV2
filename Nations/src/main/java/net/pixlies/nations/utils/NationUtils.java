package net.pixlies.nations.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Random;

/**
 * Different utilities for nations
 *
 * @author MickMMars
 */
public class NationUtils {

    public static String randomDesc() {
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
        return StringUtils.isAlphanumeric(name);
    }

}
