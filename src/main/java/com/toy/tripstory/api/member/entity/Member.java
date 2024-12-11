package com.toy.tripstory.api.member.entity;

import com.toy.tripstory.api.member.distance.MemberRole;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Member {

    private Long memberId;
    private String name;
    private MemberRole memberRole;

}
