package com.ordermgmt.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class AuthResponseFilter extends AbstractGatewayFilterFactory<AuthResponseFilter.Config> {

    private final ObjectMapper objectMapper;

    public AuthResponseFilter(ObjectMapper objectMapper) {
        super(Config.class);
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpResponse originalResponse = exchange.getResponse();

            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                @Override
                public Mono<Void> writeWith(org.reactivestreams.Publisher<? extends DataBuffer> body) {
                    if (getStatusCode() == HttpStatus.OK && body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            // Juntamos todos los chunks del body
                            DataBuffer joinedBuffer = exchange.getResponse().bufferFactory()
                                    .join(dataBuffers);
                            byte[] content = new byte[joinedBuffer.readableByteCount()];
                            joinedBuffer.read(content);
                            DataBufferUtils.release(joinedBuffer);

                            String bodyStr = new String(content, StandardCharsets.UTF_8);

                            try {
                                // Extraemos el token del body
                                Map<?, ?> bodyMap = objectMapper.readValue(bodyStr, Map.class);
                                String token = (String) bodyMap.get("token");

                                if (token != null) {
                                    // Seteamos la cookie HttpOnly
                                    ResponseCookie cookie = ResponseCookie.from("jwt", token)
                                            .httpOnly(true)
                                            .secure(false) // true en producción con HTTPS
                                            .path("/")
                                            .maxAge(86400)
                                            .sameSite("Strict")
                                            .build();

                                    getDelegate().getHeaders().add("Set-Cookie", cookie.toString());
                                }
                            } catch (Exception e) {
                                // Si no es JSON o no tiene token, dejamos pasar
                            }

                            // Devolvemos el body original sin el token
                            byte[] modifiedContent = bodyStr.getBytes(StandardCharsets.UTF_8);
                            getDelegate().getHeaders().setContentLength(modifiedContent.length);
                            return exchange.getResponse().bufferFactory().wrap(modifiedContent);
                        }));
                    }
                    return super.writeWith(body);
                }
            };

            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        };
    }

    public static class Config {}
}