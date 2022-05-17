package com.example.demo.src.coupon;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CouponService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CouponDao couponDao;
    private final JwtService jwtService;

    public CouponService(CouponDao couponDao, JwtService jwtService) {
        this.couponDao = couponDao;
        this.jwtService = jwtService;
    }

}
