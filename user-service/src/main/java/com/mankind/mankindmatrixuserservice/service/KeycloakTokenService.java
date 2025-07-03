package com.mankind.mankindmatrixuserservice.service;

import com.mankind.mankindmatrixuserservice.model.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KeycloakTokenService {

    private final WebClient webClient;

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public KeycloakTokenService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public TokenResponse getToken(String username, String password) {
        String tokenEndpoint = serverUrl
            + "/realms/" + realm
            + "/protocol/openid-connect/token";

        return webClient.post()
            .uri(tokenEndpoint)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("grant_type", "password")
                .with("client_id", clientId)
                .with("client_secret", clientSecret)
                .with("username", username)
                .with("password", password)
            )
            .retrieve()
            .bodyToMono(TokenResponse.class)
            .block();
    }

    public void revokeRefreshToken(String refreshToken) {
        String revokeEndpoint = serverUrl
            + "/realms/" + realm
            + "/protocol/openid-connect/revoke";

        webClient.post()
            .uri(revokeEndpoint)
            .body(BodyInserters
                .fromFormData("token", refreshToken)
                .with("client_id", clientId)
                .with("client_secret", clientSecret)
            )
            .retrieve()
            .toBodilessEntity()
            .block();
    }
}
