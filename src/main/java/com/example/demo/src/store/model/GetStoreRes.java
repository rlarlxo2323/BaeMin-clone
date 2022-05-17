package com.example.demo.src.store.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreRes {
    private String storeName;
    private String grade;
    private String menu;
    private String priceMin;
    private String deliveryTip;
    private String deliveryTime;
    private String path;
}
