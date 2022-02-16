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
        //TODO ADD BETTER DESCRIPTIONS
        String[] arr = new String[]{
            "No description yet :(",
            "Pixlies is cool huh",
            "Change me pleaaaase!",
            "My nation owner was too lazy to change me :("
        };

        Random r = new Random();
        int randomNumber = r.nextInt(arr.length);

        return arr[randomNumber];
    }

    public static boolean nameValid(String name) {
        return StringUtils.isAlphanumeric(name);
    }

}
