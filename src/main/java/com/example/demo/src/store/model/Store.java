package com.example.demo.src.store.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Store {
    private String storeCode;
    private String storeName;
    private String storeAddress;
    private String holiday;
    private String phone;
    private String priceMin;
    private String payment;
    private String storeInfo;
    private String storeMinDeliveryTime;
    private String storeMaxDeliveryTime;
}
