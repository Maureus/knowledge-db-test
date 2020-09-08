package com.example.knowledgedbcore.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private Long tokenId;
    private String token;
    private String type = "Bearer";
    private Long id;
    private String userName;
    private String email;
    private List<String> roles;

    public JwtResponse(Long tokenId, String accessToken, Long id, String username, String email, List<String> roles) {
        this.tokenId = tokenId;
        this.token = accessToken;
        this.id = id;
        this.userName = username;
        this.email = email;
        this.roles = roles;
    }

    public JwtResponse() {
    }
}
