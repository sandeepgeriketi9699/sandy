package com.coupon.repository;



import org.springframework.data.mongodb.repository.MongoRepository;

import com.coupon.document.CouponProductList;

public interface CouponProductListRepository extends MongoRepository<CouponProductList, String> {

	void delete(CouponProductList coupon);

}