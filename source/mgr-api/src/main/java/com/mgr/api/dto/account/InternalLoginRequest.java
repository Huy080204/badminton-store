package com.mgr.api.dto.account;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalLoginRequest {
    private String username;
    private String password;
    private String grantType;
}
