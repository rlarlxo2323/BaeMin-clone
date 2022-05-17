package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Coupon {
    private String couponCode;
    private String couponName;
    private String discount;
    private String storeCode;
    private String priceMin;
    private String startDate;
    private String endDate;
    private String period;
    private String option;
}
