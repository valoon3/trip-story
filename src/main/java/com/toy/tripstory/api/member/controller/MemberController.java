package com.toy.tripstory.api.member.controller;

import com.toy.tripstory.api.member.dto.LoginRequest;
import com.toy.tripstory.api.member.dto.MemberRegisterRequest;
import com.toy.tripstory.api.member.service.MemberService;
import com.toy.tripstory.auth.util.JwtToken;
import com.toy.tripstory.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService userService;

    @PostMapping("/login")
    public BaseResponse<JwtToken> login(@Valid @RequestBody LoginRequest request) {
        return BaseResponse.success(userService.login(request));
    }

    @PostMapping("/signup")
    public BaseResponse<Long> signup(@Valid @RequestBody MemberRegisterRequest request) {
        return BaseResponse.success(userService.signup(request));
    }

}
