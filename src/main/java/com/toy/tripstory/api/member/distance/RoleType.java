package com.toy.tripstory.api.member.distance;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum RoleType {

    ROLE_USER, ROLE_ADMIN, ROLE_SUPER_ADMIN;

    public static RoleType from(String role) {
        return RoleType.valueOf(role);
    }

    /**
     * 권한을 부여할 리스트를 생성
     */
    public static Set<RoleType> getRoleTypeList(RoleType... roleTypes) {
        return Arrays.stream(roleTypes)
                .collect(Collectors.toSet()); // 중복 제거를 위해 Set으로 변환
    }

    public String getRoleTypeName() {
        if (this == ROLE_USER) return "회원";
        else if (this == ROLE_ADMIN) return "관리자";
        else if (this == ROLE_SUPER_ADMIN) return "슈퍼 관리자";
        else throw new IllegalArgumentException("존재하지 않는 역할입니다.");
    }


}
