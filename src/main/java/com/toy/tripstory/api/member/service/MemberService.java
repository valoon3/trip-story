package com.toy.tripstory.api.member.service;

import com.toy.tripstory.api.member.distance.RoleType;
import com.toy.tripstory.api.member.dto.LoginRequest;
import com.toy.tripstory.api.member.dto.MemberRegisterRequest;
import com.toy.tripstory.api.member.entity.Member;
import com.toy.tripstory.api.member.repository.MemberRepository;
import com.toy.tripstory.api.member.repository.MemberRoleRepository;
import com.toy.tripstory.auth.TokenManager;
import com.toy.tripstory.auth.util.JwtToken;
import com.toy.tripstory.error.ErrorType;
import com.toy.tripstory.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenManager tokenManager;

    private final MemberRoleService memberRoleService;

    public JwtToken login(LoginRequest request) {
        // 사용자 조회
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(ErrorType.MEMBER_NOT_EXISTS));

        String roles = member.getRoles().stream()
                .map(role -> role.getRoleType().name())
                .reduce((role1, role2) -> role1 + "," + role2)
                .orElse("");

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new EntityNotFoundException(ErrorType.NOT_VALID_PASSWORD);
        }

        // refresh token 업데이트
        JwtToken jwtTokenDto = tokenManager.createJwtTokenDto(member.getMemberId(), roles);
        member.updateRefreshToken(jwtTokenDto.getRefreshToken(), jwtTokenDto.getRefreshTokenExpireTime());

        return jwtTokenDto;
    }

    @Transactional
    public Long signup(MemberRegisterRequest request) {
        Member member = Member.createMember(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname(),
                request.getUsername()
        );

        memberRepository.save(member);
        memberRoleService.createMemberRoles(member, RoleType.getRoleTypeList(RoleType.ROLE_USER));

        return member.getMemberId();
    }
}