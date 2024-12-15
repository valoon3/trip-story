package com.toy.tripstory.api.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRegisterRequest {
    String email;
    String password;
    String username;
    String nickname;
}
