package com.toy.tripstory.api.member.service;

import com.toy.tripstory.api.member.distance.RoleType;
import com.toy.tripstory.api.member.entity.Member;
import com.toy.tripstory.api.member.entity.Role;
import com.toy.tripstory.api.member.repository.MemberRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberRoleService {

    private final MemberRoleRepository memberRoleRepository;

    @Transactional
    public List<Role> createMemberRoles(Member member, Set<RoleType> roleTypes) {
        List<Role> roles = Role.createMemberRoles(member, roleTypes);
        return memberRoleRepository.saveAll(roles);
    }
}
