package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostOrdersReq {
    private String menuCode;
    private String optionCode;
    private String amount;
    private String address;
    private String demandStore;
    private String demandDelivery;
}
