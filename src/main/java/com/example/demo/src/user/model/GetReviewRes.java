package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewRes {
    private String storeName;
    private String menu;
    private String grade;
    private String content;
    private String path;
    private String createAt;
    private String updateAt;
}
