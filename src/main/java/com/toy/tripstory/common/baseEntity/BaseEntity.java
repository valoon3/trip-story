package com.toy.tripstory.common.baseEntity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity {

//    @CreatedBy
//    @Column(updatable = false)
//    protected Long createdBy;
//
//    @LastModifiedBy
//    @Column(nullable = false)
//    protected String modifiedBy;

}
