package com.quidsi.log.analyzing;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class LogReadUtils {

    public static String ENTER_STRING = "\n";

    @SuppressWarnings("resource")
    public static Map<String, String[]> logRead(File file, int logNum) {
        Map<Integer, String> logNameMap = new HashMap<>();
        Map<String, String[]> messagesMap = new HashMap<>();

        String fileName = file.getName();

        logNameMap.put(logNum, fileName);

        String str = "";

        InputStreamReader inputReader = null;
        BufferedReader bufferReader = null;

        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            inputReader = new InputStreamReader(inputStream);
            bufferReader = new BufferedReader(inputReader);

            // ∂¡»°“ª––
            while ((str = bufferReader.readLine()) != null) {
                String[] messages = str.split("\\|");
                messagesMap.put(fileName, messages);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return messagesMap;
    }
}
