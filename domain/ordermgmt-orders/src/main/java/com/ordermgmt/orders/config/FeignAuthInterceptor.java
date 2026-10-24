package com.ordermgmt.orders.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String username = request.getHeader("X-User-Name");
            String role = request.getHeader("X-User-Role");

            if (username != null) {
                template.header("X-User-Name", username);
            }
            if (role != null) {
                template.header("X-User-Role", role);
            }
        }
    }
}