package com.algo.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtResponse {
    @JsonProperty("accessToken")
    private String token;
    @JsonProperty("tokenType")
    private String type = "Bearer";
    private Long id;
    private String username;
    private String uuid;
    
    public JwtResponse() {
        // Default constructor
    }
    
    public JwtResponse(String token, Long id, String username, String uuid) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.uuid = uuid;
    }

    @JsonProperty("accessToken")
    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("tokenType")
    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
