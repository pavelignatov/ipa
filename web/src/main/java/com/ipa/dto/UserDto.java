package com.ipa.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDto implements Serializable {
    private String username;
    private String email;
    private String token;
    private boolean authenticated;

    public UserDto() {
    }

    public UserDto(String username, String token, boolean authenticated) {
        this.username = username;
        this.token = token;
        this.authenticated = authenticated;
    }

}
