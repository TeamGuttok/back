package com.app.guttokback.common.api;

public class ResponseMessages {
    // user
    public static final String USER_RETRIEVE_SUCCESS = "유저 상세 조회 성공";
    public static final String USER_SAVE_SUCCESS = "유저 저장 성공";
    public static final String PASSWORD_UPDATE_SUCCESS = "비밀번호 수정 성공";
    public static final String NICKNAME_UPDATE_SUCCESS = "닉네임 수정 성공";
    public static final String ALARM_UPDATE_SUCCESS = "알림 수정 성공";
    public static final String USER_DELETE_SUCCESS = "유저 삭제 성공";
    public static final String CERTIFICATION_NUMBER_SUCCESS = "비밀번호 찾기 인증 성공";
    public static final String EMAIL_VERIFICATIOIN_SUCCESS = "이메일 인증 성공";

    // userSubscription
    public static final String USER_SUBSCRIPTION_SAVE_SUCCESS = "구독항목 저장 성공";
    public static final String USER_SUBSCRIPTION_UPDATE_SUCCESS = "구독항목 수정 성공";
    public static final String USER_SUBSCRIPTION_DELETE_SUCCESS = "구독항목 삭제 성공";
    public static final String SUBSCRIPTION_LIST_SUCCESS = "구독 서비스 응답 성공";

    // auth
    public static final String USER_LOGIN_SUCCESS = "사용자 로그인 성공";
    public static final String USER_LOGOUT_SUCCESS = "사용자 로그아웃 성공";
    public static final String SESSION_VALID = "유효 세션";

    // mail
    public static final String CERTIFICATION_EMAIL_SEND_SUCCESS = "인증코드 이메일 발송 성공";

    // group
    public static final String SUBSCRIPTION_GROUP_SAVE_SUCCESS = "그룹 생성 성공";

    // notification
    public static final String NOTIFICATION_READ_SUCCESS = "알림 읽음 처리 성공";
    public static final String NOTIFICATION_DELETE_SUCCESS = "알림 삭제 처리 성공";
}
