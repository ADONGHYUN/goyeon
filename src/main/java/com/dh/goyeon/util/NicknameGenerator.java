package com.dh.goyeon.util;

import java.util.Random;

public class NicknameGenerator {
    private static final String[] adjectives = {
        "귀여운", "멋진", "행복한", "용감한", "재빠른", "느긋한", "영리한"
    };

    private static final String[] nouns = {
        "강아지", "고양이", "사자", "토끼", "다람쥐", "호랑이", "곰", "펭귄"
    };

    private static final Random random = new Random();

    public static String generate() {
        String adjective = adjectives[random.nextInt(adjectives.length)];
        String noun = nouns[random.nextInt(nouns.length)];
        int code = 1000 + random.nextInt(9000); // 1000~9999 사이 숫자

        return adjective + noun + "#" + code;
    }
}
