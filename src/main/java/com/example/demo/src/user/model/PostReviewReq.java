package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostReviewReq {
    private String orderCode;
    private String content;
    private int grade;
    private String originFile;
    private String saveFile;
}
