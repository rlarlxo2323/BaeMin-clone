package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCouponRes {
    private String discount;
    private String couponName;
    private String priceMin;
    private String options;
    private String usePeriod;
}
