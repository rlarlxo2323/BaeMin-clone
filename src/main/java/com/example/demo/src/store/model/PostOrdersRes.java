package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostOrdersRes {
    private String storeName;
    private String menuName;
    private String price;
    private String orderCode;
    private String userAddress;
    private String orderTime;
    private String flag;
    private String amount;
    private String option;
}
