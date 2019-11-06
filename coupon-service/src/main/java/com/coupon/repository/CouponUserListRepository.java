package com.coupon.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.coupon.document.CouponUserList;

public interface CouponUserListRepository extends MongoRepository<CouponUserList, String> {

}