package com.eh.stock.user.domain;

public enum UserStatus {
    GUEST("게스트유저"),
    LOGIN("로그인유저");

    private final String value;

    UserStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
