package hu._ig.crm.crm4ig.model;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String username;
    private String password;
}
