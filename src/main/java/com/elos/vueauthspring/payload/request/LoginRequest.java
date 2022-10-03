package com.elos.vueauthspring.payload.request;

import java.util.Set;

public class LoginRequest {

    private String token;
    private Set<String> role;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}
