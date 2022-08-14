package net.pixlies.nations.utils;

import net.pixlies.nations.locale.NationsLang;

public final class NationTextUtils {

    public static String getChunkLocationFormatted(int x, int z) {
        return NationsLang.CHUNK_LOCATION_FORMAT.get()
                .replace("%X%", String.valueOf(x))
                .replace("%Z%", String.valueOf(z));
    }

}
