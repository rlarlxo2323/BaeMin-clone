package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    String id;
    String password;
    String name;
    String nickname;
    String phone;
    String email;
    String address;
}
