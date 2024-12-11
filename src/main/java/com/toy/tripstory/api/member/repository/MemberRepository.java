package com.toy.tripstory.api.member.repository;

import com.toy.tripstory.api.member.entity.Member;

public interface MemberRepository {

    void save(Member member);

    Member findById(Long memberId);

}
