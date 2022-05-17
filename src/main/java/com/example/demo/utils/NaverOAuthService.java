package com.example.demo.utils;

import com.example.demo.config.secret.Secret;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

@Service
public class NaverOAuthService {
    public String getRequestLoginUrl(){

        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString(32);

        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.add("response_type", "code");
        requestParam.add("state", state);
        requestParam.add("client_id", Secret.CLIENT_ID);
        requestParam.add("redirect_uri", Secret.REDIRECT_URI);

        return UriComponentsBuilder.fromUriString(Secret.BASE_URI)
                .queryParams(requestParam)
                .build().encode()
                .toString();
    }

    public ResponseEntity<?> requestAccessToken(String code, String state) {

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("state", state);
        requestBody.add("client_id", Secret.CLIENT_ID);
        requestBody.add("client_secret", Secret.CLIENT_SECRET);
        requestBody.add("grant_type", "authorization_code");

        return new RestTemplate().postForEntity(Secret.TOKEN_URI, requestBody, Map.class);
    }
}
