package com.yonyou.x.baidu.speech;

import com.github.stuxuhai.jpinyin.PinyinException;

import java.util.List;

import static com.yonyou.x.baidu.speech.PinyinSearch.ERR_SCORE;

public class CommandAdapter {

    private PinyinSearch search = new PinyinSearch();

    private int score = 10;

    public void importCommand(String[] cmds) {
        try {
            search.addWord(cmds);
        } catch (PinyinException e) {
            e.printStackTrace();
        }
    }

    public String checkCommand(String cmd) {
        try {
            List<Score> rs = search.search(cmd, 1);
            if (rs.size() > 0 && rs.get(0).score <= score) {
                return rs.get(0).word.word;
            }
        } catch (PinyinException e) {
            e.printStackTrace();
        }
        return "";
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
