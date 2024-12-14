package com.toy.tripstory.auth.util;

import com.toy.tripstory.error.ErrorType;
import com.toy.tripstory.error.exception.UnauthorizedException;
import org.springframework.util.StringUtils;

public class AuthorizationHeaderUtils {

    public static void validateAuthorization(String authorizationHeader) {

        // 1. authorizationHeader 필수 체크
        if(!StringUtils.hasText(authorizationHeader)) {
            throw new UnauthorizedException(ErrorType.NOT_EXISTS_AUTHORIZATION);
        }

        // 2. authorizationHeader Bearer 체크
        String[] authorizations = authorizationHeader.split(" ");
        if(authorizations.length < 2 || (!"Bearer".equals(authorizations[0]))) {
            throw new UnauthorizedException(ErrorType.NOT_VALID_BEARER_GRANT_TYPE);
        }
    }

}
