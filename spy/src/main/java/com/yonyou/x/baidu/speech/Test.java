package com.yonyou.x.baidu.speech;

import com.github.stuxuhai.jpinyin.PinyinException;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class Test {

    private static String file = "/Users/xyy/Downloads/audiobd_speech_sdk_asr_v3.0.7.3_bdasr_20180313_726f26e/app/src/main/assets/names.txt";
    private static String date_file = "/Users/xyy/Downloads/audiobd_speech_sdk_asr_v3.0.7.3_bdasr_20180313_726f26e/app/src/main/assets/datetime.txt";

    static String[] bak = new String[]{
            "阳林正文博两根玉周雷",
            "杨凌征文博日两根一周内将增压",
            "阳林正文博两根一周磊将军鸭",
            "今天",
            "阳林正文博"};

    private static String testStr = bak[3];

    public static void main(String[] args) throws IOException, PinyinException {
        checkDatetime();
    }

    private static void testCommand() {
        CommandAdapter adapter = new CommandAdapter();
        adapter.importCommand(new String[]{"下一页","退出","关闭","下二页","下三页","下四页","下五页","上一页"});
        adapter.setScore(4);
        System.out.println("结果："+adapter.checkCommand("管笔"));
    }

    private static void checkNames() throws IOException, PinyinException {
        JSONObject value = new JSONObject();
        value.put("name", testStr);
        XTelephoneRecognization.require(new FileInputStream(file));

        System.out.println("start");
        long start = System.currentTimeMillis();

        String rs = XTelephoneRecognization.parser("查找" + testStr + "的联系方式", "test", value);

        long stamp = System.currentTimeMillis() - start;

        System.out.println("用时：" + stamp);
        System.out.println("输入：" + testStr);
        System.out.println("结果：" + rs);
    }

    private static void checkDatetime() throws IOException, PinyinException {
        JSONObject value = new JSONObject();
        value.put("date", testStr);
        XDateTimeRecognization.require(new FileInputStream(date_file));

        System.out.println("start");
        long start = System.currentTimeMillis();

        String rs = XDateTimeRecognization.parser("查找" + testStr + "的日程", "test", value);

        long stamp = System.currentTimeMillis() - start;

        System.out.println("用时：" + stamp);
        System.out.println("输入：" + testStr);
        System.out.println("结果：" + rs);
    }
}
