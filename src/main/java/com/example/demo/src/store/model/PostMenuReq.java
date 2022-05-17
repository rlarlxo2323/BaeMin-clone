package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostMenuReq {
    private String menuCode;
    private String menuName;
    private String price;
    private String menuInfo;
    private String tag;
    private String path;
}
