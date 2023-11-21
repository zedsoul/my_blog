package com.zlc.blogencrypt.filter;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zlc.blogencrypt.Utils.JWTUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
@Component
public class JWTFilter implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("z-Auth");
        try {
            JWTUtils.verify(token);
            // 如果验证通过，继续请求处理链
            return chain.filter(exchange);
        } catch (SignatureVerificationException e) {
            return errorResponse(exchange, "无效签名！", HttpStatus.UNAUTHORIZED);
        } catch (TokenExpiredException e) {
            return errorResponse(exchange, "Token过期！", HttpStatus.UNAUTHORIZED);
        } catch (AlgorithmMismatchException e) {
            return errorResponse(exchange, "Token算法不一致！", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return errorResponse(exchange, "Token无效！", HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> errorResponse(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");

        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("message", message);

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] responseMessageBytes = new byte[0];
        try {
            responseMessageBytes = objectMapper.writeValueAsBytes(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return response.writeWith(Mono.just(responseMessageBytes).map(b -> response.bufferFactory().wrap(b)));
    }
}
