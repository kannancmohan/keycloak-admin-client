package com.kcm.test.service.keycloak;

import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakPartialImportService {

  private final Keycloak keycloak;

  @Value("${keycloak.realm}")
  private String realm;

  public Response importUsers(final PartialImportRepresentation importRepresentation) {
    return keycloak.realm(realm).partialImport(importRepresentation);
  }
}
