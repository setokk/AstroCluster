package edu.setokk.astrocluster.util;

import org.springframework.lang.NonNull;

public final class StringUtils {

    /**
     * Splits a String based on a regex and returns the last occurring element.
     * @param s the string to be split
     * @param regex the regex
     * @return the last occurring element of the split string
     */
    public static String splitByAndGetLast(@NonNull String s, @NonNull String regex) {
        String[] split = s.split(regex);
        return split[split.length - 1];
    }

    /**
     * Splits a String based on a regex and returns the first occurring element.
     * @param s the string to be split
     * @param regex the regex
     * @return the first occurring element of the split string
     */
    public static String splitByAndGetFirst(@NonNull String s, @NonNull String regex) {
        String[] split = s.split(regex);
        return split[0];
    }
}
