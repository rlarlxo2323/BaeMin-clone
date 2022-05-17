package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreInfoRes {
    private String brandName;
    private String holiday;
    private String phone;
    private String hours;
}
