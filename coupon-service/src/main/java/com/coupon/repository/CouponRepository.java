package com.coupon.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.coupon.document.Coupon;

public interface CouponRepository extends MongoRepository<Coupon, String> {

}
