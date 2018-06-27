package com.yonyou.x.baidu.speech;

import com.github.stuxuhai.jpinyin.PinyinException;

import org.json.JSONArray;
import org.json.JSONObject;

public class XRecognization {
    public static String paser(String result, JSONObject recognition) {
        JSONObject nlu = new JSONObject(recognition.optString("results_nlu"));
        if (nlu == null) {
            return result;
        }
        JSONArray rs = nlu.optJSONArray("results");
        if (rs == null || rs.length() < 1) {
            return result;
        }
        JSONObject rs1 = rs.getJSONObject(0);

//        {
//            "raw_text":"打电话给两个月",
//                "results":[
//            {
//                "domain":"telephone",
//                    "intent":"call",
//                    "parser":"bsg",
//                    "object":{
//                "name":"两个月"
//            }
//            }
//            ]
//        }
        String domain = rs1.optString("domain");
        String intent = rs1.optString("intent");
        JSONObject obj = rs1.optJSONObject("object");

        //TODO:根据 Domain 和 Intent 去找寻处理的类
        try {
            switch (domain) {
                case "schedule":
                    return XDateTimeRecognization.parser(result, intent, obj);
                case "app":
                    return XAppRecognization.parser(result, intent, obj);
                default:
                    return XTelephoneRecognization.parser(result, intent, obj);
            }


        } catch (PinyinException e) {
            e.printStackTrace();
        }

        return result;
    }
}
