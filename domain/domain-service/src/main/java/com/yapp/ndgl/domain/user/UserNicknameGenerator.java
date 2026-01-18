package com.yapp.ndgl.domain.user;

import java.util.List;
import java.util.Random;

public final class UserNicknameGenerator {

    private static final List<String> ADJECTIVES = List.of(
        "행복한", "즐거운", "신나는", "평화로운", "아름다운", "멋진", "따뜻한",
        "상쾌한", "활기찬", "편안한", "로맨틱한", "모험적인", "자유로운", "감성적인"
    );

    private static final List<String> NOUNS = List.of(
        "강아지", "고양이", "토끼", "햄스터", "다람쥐", "여우", "늑대",
        "곰", "사자", "호랑이", "표범", "치타", "코끼리", "기린",
        "얼룩말", "사슴", "팬더", "펭귄", "오리", "백조", "독수리",
        "올빼미", "참새", "비둘기", "까마귀", "까치", "앵무새", "공작",
        "물고기", "고래", "돌고래", "상어", "문어", "오징어", "해파리",
        "나비", "벌", "잠자리", "개미", "무당벌레", "사슴벌레", "장수풍뎅이"
    );

    private static final Random RANDOM = new Random();

    public static String generate() {
        String adjective = ADJECTIVES.get(RANDOM.nextInt(ADJECTIVES.size()));
        String noun = NOUNS.get(RANDOM.nextInt(NOUNS.size()));
        return adjective + " " + noun;
    }
}
