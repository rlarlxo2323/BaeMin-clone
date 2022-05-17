package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostOptionReq {
    private String optionName;
    private int additionalPrice;
    private String category;
    private String optionCode;
}
