package com.toy.tripstory.api.member.entity;

import com.toy.tripstory.api.member.distance.RoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // Member와의 다대일 관계 설정
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Role(Member member, RoleType roleType) {
        this.roleType = roleType;
        this.member = member;
    }

    public static List<Role> createMemberRoles(Member member, Set<RoleType> roleType) {
        return roleType.stream()
                .map(r -> new Role(member, r))
                .toList();
    }


}
