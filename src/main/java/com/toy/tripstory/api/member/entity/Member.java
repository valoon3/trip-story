package com.toy.tripstory.api.member.entity;

import com.toy.tripstory.common.util.DateUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "members",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_email",
                        columnNames = {"email"}
                )
        })
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    // 이메일 로그인 방식
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private boolean isLocked;

    @Column(length = 250)
    private String refreshToken;

    private LocalDateTime tokenExpirationTime;

    // MemberRole과의 일대다 관계 설정
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Role> roles;

    public void updateRefreshToken(String refreshToken, Date refreshTokenExpireTime) {
        this.refreshToken = refreshToken;
        this.tokenExpirationTime = DateUtils.convertToLocalDateTime(refreshTokenExpireTime);
    }

    public static Member createMember(
            String email,
            String password,
            String nickname,
            String username
    ) {
        return Member.builder()
                .email(email)
                .password(password)
                .username(username)
                .nickname(nickname)
                .isLocked(true)
                .build();
    }

}
