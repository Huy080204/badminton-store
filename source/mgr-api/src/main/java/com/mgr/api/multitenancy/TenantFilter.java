package com.mgr.api.multitenancy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.mgr.api.constant.MgrConstant.TENANT_HEADER;

@Component
@Slf4j
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tenant = request.getHeader(TENANT_HEADER);
        log.debug("Request received with X-tenant header: {}", tenant);
        if (tenant != null && !tenant.isEmpty()) {
            TenantContext.setCurrentTenant(tenant);
        } else {
            TenantContext.setCurrentTenant("default");
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
