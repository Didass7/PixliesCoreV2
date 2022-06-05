package net.pixlies.core.utils;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * i spent too long on this
 * color does not work, thats not a bug
 * @author Dynmie
 */
public class TextScroller {

    private final @Getter String message; // "Test Message     " ("Test Message", Space 5)
    private final @Getter int width;

    private int pos = 0;

    public TextScroller(String message, int width, int spaceBetween) {

        // Check if message is null
        Objects.requireNonNull(message);

        if (message.length() < 1)
            throw new NumberFormatException("Message length cannot be lower than 1");

        if (width < 1)
            throw new NumberFormatException("width cannot be lower than 1");

        if (spaceBetween < 0)
            throw new NumberFormatException("spaceBetween cannot be lower than 0");

        String newMessage = message;

        // if message is smaller than the width then add spaces
        if (newMessage.length() < width) {
            StringBuilder builder = new StringBuilder(newMessage);
            while (builder.length() < width) {
                builder.append(" ");
            }
            newMessage = builder.toString();
        }

        // add spaces
        StringBuilder spaceBuilder = new StringBuilder(newMessage);
        while (spaceBuilder.length() < newMessage.length() + spaceBetween) {
            spaceBuilder.append(" ");
        }
        newMessage = spaceBuilder.toString();

        this.message = newMessage;
        this.width = width;

    }

    public String next() {

        String newMessage = message + message;

        int endPos = message.length();

        String toReturn = newMessage.substring(pos, pos + width);
        pos++;

        if (pos > endPos) {
            pos = 0;
        }

        return toReturn;

    }

}