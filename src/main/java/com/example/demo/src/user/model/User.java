package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private String id;
    private String password;
    private String name;
    private String nickname;
    private String phone;
    private String email;
    private String address;
    private String point;
    private String rating;
    private String join;
}
