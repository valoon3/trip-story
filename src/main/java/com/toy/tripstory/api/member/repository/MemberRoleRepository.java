package com.toy.tripstory.api.member.repository;

import com.toy.tripstory.api.member.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<Role, Long> {

}
