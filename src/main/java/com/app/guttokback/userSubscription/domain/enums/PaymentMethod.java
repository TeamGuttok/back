package com.app.guttokback.userSubscription.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    CARD("카드"),
    BANK_TRANSFER("계좌이체"),
    MOBILE_PAYMENT("휴대폰 결제"),
    NAVER_PAY("네이버 페이"),
    KAKAO_PAY("카카오 페이"),
    OTHER("기타");

    private final String method;
}
