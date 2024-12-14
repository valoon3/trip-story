package com.toy.tripstory.member;

import com.toy.tripstory.api.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    MemberService memberService = new MemberService();

    @Test
    void 테스트() {
        memberService.test();
//        Assertions.assertThat(memberService.test()).isEqualTo(10);
    }

}
