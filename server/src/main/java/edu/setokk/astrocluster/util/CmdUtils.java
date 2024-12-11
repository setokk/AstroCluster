package edu.setokk.astrocluster.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CmdUtils {
    public static final int EXIT_CODE_SUCCESS = 0;
    public static final int EXIT_CODE_FATAL_ERROR = -1;

    public static int executeCmd(String... command) {
        try {
            Process process = Runtime.getRuntime().exec(command);

            Thread.ofVirtual().start(() -> streamOutput(process.getInputStream(), "OUTPUT"));
            Thread.ofVirtual().start(() -> streamOutput(process.getErrorStream(), "ERROR"));

            int status = process.waitFor();
            return status;
        } catch (IOException|InterruptedException e) {
            return EXIT_CODE_FATAL_ERROR;
        }
    }

    private static void streamOutput(InputStream inputStream, String type) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[" + type + "] " + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
