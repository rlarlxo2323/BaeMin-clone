package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPointRes {
    private String storeName;
    private String point;
    private String date;
    private String expiration;
}
