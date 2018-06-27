package com.yonyou.x.baidu.speech;

public class Score implements Comparable {
    Word word;
    int score;

    public Score() {

    }

    public Score(int s) {
        this.score = s;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Score) {
            return score - ((Score) o).score;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "{" +
                "word=" + word +
                ", score=" + score +
                '}';
    }
}