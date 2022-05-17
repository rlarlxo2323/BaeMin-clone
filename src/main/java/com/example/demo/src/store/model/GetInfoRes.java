package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetInfoRes {
    private String storeName;
    private String priceMin;
    private String payment;
    private String deliveryTip;
    private String deliveryTime;
    private String favorite;
    private String review;
}
