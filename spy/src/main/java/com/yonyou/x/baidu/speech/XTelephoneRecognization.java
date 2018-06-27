package com.yonyou.x.baidu.speech;

import com.github.stuxuhai.jpinyin.PinyinException;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class XTelephoneRecognization {

    private static final int STD_SCORE = 10;
    private static final int BEST_SCORE = 10;
    static PinyinSearch search = new PinyinSearch();

    public static String parser(String result, String intent, JSONObject value) throws PinyinException {
        String rs = replaceName(result, value.optString("name"));
        rs = replaceName(rs, value.optString("name2"));
        return rs;
    }

    private static String replaceName(String result, String name) throws PinyinException {
        if (name != null && !"".equals(name)) {
            if (name.length() > 3) {
                return replaceNName(result, name);
            }
            Score rs = search.searchOne(name);
            if (rs != null) {
                if (rs.score < STD_SCORE) {
                    return result.replace(name, rs.word.word);
                }
            }
        }
        return result;
    }

    private static String replaceNName2(String result, String name) throws PinyinException {
        if (name.length() >= 3) {
            String first = name.substring(0, 3);
            Score rs = search.searchOne(first);
            if (rs != null && rs.score < BEST_SCORE) {
                result = result.replace(first, rs.word.word);
                return replaceNName(result, name.substring(3));
            } else {
                String n2 = name.substring(0, 2);
                Score s2 = search.searchOne(n2);
                if (s2 != null && s2.score < BEST_SCORE) {
                    result = result.replace(n2, s2.word.word);
                    return replaceNName(result, name.substring(2));
                }
            }
        } else if (name.length() >= 2) {
            String n2 = name.substring(0, 2);
            Score s2 = search.searchOne(n2);
            if (s2 != null && s2.score < BEST_SCORE) {
                result = result.replace(n2, s2.word.word);
                return replaceNName(result, name.substring(2));
            }
        }
        return result;
    }

    private static String bestRs = "";
    private static int bestScore = 99999;

    private static String replaceNNameSlow(String result, String name) throws PinyinException {
        int size = name.length();
        bestRs = "";
        bestScore = 99999;
        getNameColl(0, "", size, name);
        if (bestRs.length() > 0) {
            return result.replace(name, bestRs);
        } else {
            return result;
        }
    }


    public static class ResultScore {

        public String word;
        public int score;

        public ResultScore(String w, int s) {
            this.word = w;
            this.score = s;
        }

        @Override
        public String toString() {
            return word + "[" + score + "]";
        }
    }

    private static void getNameColl(int start, String startStr, int max_count, String value) throws PinyinException {
        for (int i = 1; i <= 4; i++) {
            String sign = startStr + i;
            if (i + start >= max_count) {
                ResultScore score = getScoreBySign(sign, value);
                if (score.score < bestScore) {
                    bestScore = score.score;
                    bestRs = score.word;
                    System.out.println("当前最佳：" + score.toString());
                }
                break;
            }
            getNameColl(i + start, sign, max_count, value);
        }
    }

    private static ResultScore getScoreBySign(String sign, String value) throws PinyinException {
        String[] rsa = getRsArrayBySign(sign, value);
        int score = 0;
        String word = "";
        for (int i = 0; i < rsa.length; i++) {
            Score s = search.searchOne(rsa[i]);
            int sss = (s.score == PinyinSearch.ERR_SCORE ? 15 : s.score);
            if (s.score != PinyinSearch.ERR_SCORE && s.score > BEST_SCORE) {
                sss += 5;
            }
            score += sss;
            word += (s.score == PinyinSearch.ERR_SCORE ? rsa[i] : s.word.word);
        }
        return new ResultScore(word, score);
    }

    private static String getRsBySign(String sign, String value) {
        String[] rsa = getRsArrayBySign(sign, value);
        String rs = "";
        for (int i = 0; i < rsa.length; i++) {
            rs += rsa[i];
        }
        return rs;
    }

    private static String[] getRsArrayBySign(String sign, String value) {
        int startIndex = 0;
        String[] rs = new String[sign.length()];
        for (int i = 0; i < sign.length(); i++) {
            int c = Integer.parseInt(sign.substring(i, i + 1));
            rs[i] = value.substring(startIndex, startIndex + c);
            startIndex += c;
        }
        return rs;
    }

    private static String replaceNName(String result, String name) throws PinyinException {
        if (name.length() > 3) {
            String n1 = name.substring(0, 3);
            String n2 = name.substring(0, 2);

            Score r1 = search.searchOne(n1);
            Score r2 = search.searchOne(n2);

            if (r1.score <= r2.score + 2) {
                if (r1.score < BEST_SCORE) {
                    result = result.replace(n1, r1.word.word);
                }
                return replaceNName(result, name.substring(3));
            } else {
                if (r2.score < BEST_SCORE) {
                    result = result.replace(n2, r2.word.word);
                }
                return replaceNName(result, name.substring(2));
            }
        } else if (name.length() == 3) {
            String n1 = name.substring(0, 3);
            Score r1 = search.searchOne(n1);
            if (r1.score < BEST_SCORE) {
                result = result.replace(n1, r1.word.word);
            }
            return replaceNName(result, name.substring(3));
        } else if (name.length() >= 2) {
            String n2 = name.substring(0, 2);
            Score s2 = search.searchOne(n2);
            if (s2.score < BEST_SCORE) {
                result = result.replace(n2, s2.word.word);
            }
            return replaceNName(result, name.substring(2));
        }
        return result;
    }

    public static void require(InputStream is) throws IOException, PinyinException {
        search.addWord(is);
    }

    public static void require(String[] names) throws PinyinException {
        search.addWord(names);
    }
}
