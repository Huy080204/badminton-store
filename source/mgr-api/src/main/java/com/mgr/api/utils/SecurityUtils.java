package com.mgr.api.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Map;

public class SecurityUtils {
    public static Long getCurrentUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof OAuth2Authentication) {
            OAuth2Authentication oauth = (OAuth2Authentication) auth;
            Map<String, Object> details = (Map<String, Object>) oauth.getUserAuthentication().getDetails();
            // Lấy đúng key "user_id" đã show ở trên
            if (details.containsKey("user_id")) {
                return Long.valueOf(details.get("user_id").toString());
            }
        }
        return null;
    }
}
