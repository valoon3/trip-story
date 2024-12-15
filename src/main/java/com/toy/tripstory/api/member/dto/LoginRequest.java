package com.toy.tripstory.api.member.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    String email;
    String password;
}
