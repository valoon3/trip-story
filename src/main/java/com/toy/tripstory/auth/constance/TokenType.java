package com.toy.tripstory.auth.constance;

import lombok.Getter;

@Getter
public enum TokenType {

    ACCESS, REFRESH;

    public static boolean isAccessToken(String tokenType) {
        return TokenType.ACCESS.name().equals(tokenType);
    }

}
