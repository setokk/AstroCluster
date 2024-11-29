package edu.setokk.astrocluster.util;

import java.io.IOException;

public class CmdUtils {
    public static final int EXIT_CODE_SUCCESS = 0;
    public static final int EXIT_CODE_FATAL_ERROR = -1;

    public static int executeCmd(String... command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            return process.waitFor();
        } catch (IOException|InterruptedException e) {
            return EXIT_CODE_FATAL_ERROR;
        }
    }
}
