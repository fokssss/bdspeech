package com.yonyou.x.baidu.speech;

import com.github.stuxuhai.jpinyin.PinyinException;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class XAppRecognization {

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
        String name = value.optString("name");
        String newname = name;
        newname = newname.replace("a","哎");
        newname = newname.replace("b","比");
        newname = newname.replace("c","色");
        newname = newname.replace("v","味");
        newname = newname.replace("u","唷");
        String result2 = result.replace(name,newname);
        String rs = replace(result2, newname);
        if(rs.equals(result2)) {
            return result;
        }
        return rs;
    }

    private static String replace(String result, String name) throws PinyinException {
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
