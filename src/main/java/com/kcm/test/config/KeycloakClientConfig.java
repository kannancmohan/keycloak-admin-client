package com.kcm.test.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KeycloakSpringBootProperties.class)
public class KeycloakClientConfig {

  @Bean
  public Keycloak keycloak(final KeycloakSpringBootProperties props) {
    return KeycloakBuilder.builder()
        .serverUrl(props.getAuthServerUrl())
        .realm(props.getRealm())
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .clientId(props.getResource())
        .clientSecret((String) props.getCredentials().get("secret"))
        .build();
  }
}
