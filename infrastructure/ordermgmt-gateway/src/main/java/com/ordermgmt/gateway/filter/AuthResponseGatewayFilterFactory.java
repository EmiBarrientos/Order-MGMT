package com.ordermgmt.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
public class AuthResponseGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthResponseGatewayFilterFactory.Config> {

    private final ModifyResponseBodyGatewayFilterFactory modifyResponseBodyFilter;
    private final ObjectMapper objectMapper;

    public AuthResponseGatewayFilterFactory(
            ModifyResponseBodyGatewayFilterFactory modifyResponseBodyFilter,
            ObjectMapper objectMapper) {
        super(Config.class);
        this.modifyResponseBodyFilter = modifyResponseBodyFilter;
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return modifyResponseBodyFilter.apply(c -> c
                .setInClass(String.class)
                .setOutClass(String.class)
                .setRewriteFunction(String.class, String.class, (exchange, body) -> {

                    try {
                        Map<?, ?> bodyMap = objectMapper.readValue(body, Map.class);
                        String token = (String) bodyMap.get("token");

                        if (token != null) {
                            log.info(">>> Token encontrado, seteando cookie");
                            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                                    .httpOnly(true)
                                    .secure(false)
                                    .path("/")
                                    .maxAge(86400)
                                    .sameSite("Strict")
                                    .build();

                            exchange.getResponse().addCookie(cookie);
                            bodyMap.remove("token");

                            try {
                                return Mono.just(objectMapper.writeValueAsString(bodyMap));
                            } catch (Exception e) {
                                log.error(">>> Error serializando body: {}", e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        log.error(">>> Error procesando body: {}", e.getMessage());
                    }
                    return Mono.just(body);
                })
        );
    }

    public static class Config {}
}