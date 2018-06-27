package com.yonyou.x.baidu.speech;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class ConvertName {
    private static String file = "/Users/xyy/Downloads/audiobd_speech_sdk_asr_v3.0.7.3_bdasr_20180313_726f26e/app/src/main/assets/username2.txt";
    private static String outfile = "/Users/xyy/Downloads/audiobd_speech_sdk_asr_v3.0.7.3_bdasr_20180313_726f26e/app/src/main/assets/user_out.txt";

    private static HashMap<String, Boolean> names = new HashMap<>();

    public static void main(String[] args) throws IOException {
        FileInputStream is = new FileInputStream(file);
        FileOutputStream out = new FileOutputStream(outfile, false);

        OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        InputStreamReader read = new InputStreamReader(is, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        while ((lineTxt = bufferedReader.readLine()) != null) {
            if (lineTxt == null || "".equals(lineTxt)) {
                continue;
            }
            lineTxt = lineTxt.trim();
            if (lineTxt.contains(" ")) {
                int index = lineTxt.indexOf(" ");
                while (index >= 0) {
                    lineTxt = lineTxt.substring(0, index) + lineTxt.substring(index + 1, lineTxt.length());
                    index = lineTxt.indexOf(" ");
                }
            }
            if (lineTxt.length() >= 4) {
                continue;
            }
            if (lineTxt.contains("先生")) {
                continue;
            }
            if (lineTxt.contains("老师")) {
                continue;
            }
            if (lineTxt.length() == 4) {
                System.out.println(lineTxt);
            }
            if (lineTxt.length() < 2) {
                continue;
            }
            if (names.containsKey(lineTxt)) {
                continue;
            }
            names.put(lineTxt, true);
            bufferedWriter.write(lineTxt + "\r");
        }
        writer.close();
        read.close();
        System.out.println(names.size());
    }
}
