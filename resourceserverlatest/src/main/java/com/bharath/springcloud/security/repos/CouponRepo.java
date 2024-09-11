package com.bharath.springcloud.security.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bharath.springcloud.security.entities.Coupon;


public interface CouponRepo extends JpaRepository<Coupon, Long> {

	Coupon findByCode(String code);

}
