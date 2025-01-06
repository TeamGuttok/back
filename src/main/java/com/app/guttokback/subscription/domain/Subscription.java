package com.app.guttokback.subscription.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Subscription {

    CUSTOM_INPUT("직접 입력"),
    YOUTUBE_PREMIUM("유튜브 프리미엄"),
    NETFLIX("넷플릭스"),
    SPOTIFY("스포티파이"),
    APPLE_MUSIC("애플 뮤직"),
    COUPANG_WOW("쿠팡 와우"),
    WAVVE("웨이브"),
    WATCHA("왓챠"),
    TVING("티빙"),
    DISNEY_PLUS("디즈니 플러스"),
    APPLE_TV("애플 티비"),
    LAFTEL("라프텔"),
    MELON("멜론"),
    GENIE("지니"),
    FLO("플로"),
    AWS("아마존 웹 서비스"),
    GCP("구글 클라우드 플랫폼"),
    CHAT_GPT("Chat GPT"),
    CLAUDE_AI("Claude AI"),
    GEMINI("제미니"),
    PERPLEXITY("퍼플렉시티"),
    SPOTV_NOW("스포티비 NOW");

    private final String name;
}
