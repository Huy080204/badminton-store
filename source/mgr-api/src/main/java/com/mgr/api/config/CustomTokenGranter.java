package com.mgr.api.config;

import com.mgr.api.service.impl.UserServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

public class CustomTokenGranter extends AbstractTokenGranter {
    private UserServiceImpl userService;
    private AuthenticationManager authenticationManager;

    protected CustomTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    public CustomTokenGranter(AuthenticationManager authenticationManager,
                              AuthorizationServerTokenServices tokenServices,
                              ClientDetailsService clientDetailsService,
                              OAuth2RequestFactory requestFactory,
                              UserServiceImpl userService,
                              String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        return super.getOAuth2Authentication(client, tokenRequest);
    }

    protected OAuth2AccessToken getAccessToken(ClientDetails client, TokenRequest tokenRequest) {
        String grantType = tokenRequest.getGrantType();

        String username = tokenRequest.getRequestParameters().get("username");
        String password = tokenRequest.getRequestParameters().get("password");
        String tenant = tokenRequest.getRequestParameters().get("tenant");
        String email = tokenRequest.getRequestParameters().get("email");

        try {
            if (SecurityConstant.GRANT_TYPE_USER.equalsIgnoreCase(grantType)) {
                return userService.getAccessTokenForEmail(client,
                        tokenRequest, email, password,
                        tenant, grantType,
                        this.getTokenServices());
            } else if (SecurityConstant.GRANT_TYPE_SELLER.equalsIgnoreCase(grantType)) {
                return userService.getAccessTokenForSeller(client, tokenRequest, username, password, tenant, grantType, this.getTokenServices());
            } else if (SecurityConstant.GRANT_TYPE_CUSTOM.equalsIgnoreCase(grantType)) {
                return userService.getAccessTokenForCustom(client, tokenRequest, username, password, tenant, grantType, this.getTokenServices());
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new InvalidTokenException("Account or tenant invalid");
        }

        return super.getAccessToken(client, tokenRequest);
    }
}
