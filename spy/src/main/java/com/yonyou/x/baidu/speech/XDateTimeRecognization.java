package com.yonyou.x.baidu.speech;

import com.github.stuxuhai.jpinyin.PinyinException;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class XDateTimeRecognization {

    private static final int STD_SCORE = 10;

    static PinyinSearch search = new PinyinSearch();

    public static void require(
            InputStream is) throws IOException, PinyinException {
        search.addWord(is);
    }

    public static void require(
            String[] names) throws PinyinException {
        search.addWord(names);
    }

    public static String parser(String result, String intent, JSONObject value) throws PinyinException {
        String rs = replaceDateTime(result, value.optString("date"));
        return rs;
    }

    private static String replaceDateTime(String result, String name) throws PinyinException {
        if (name != null && !"".equals(name)) {
            Score rs = search.searchOne(name);
            if (rs != null) {
                if (rs.score < STD_SCORE) {
                    return result.replace(name, rs.word.word);
                }
            }
        }
        return result;
    }
}
