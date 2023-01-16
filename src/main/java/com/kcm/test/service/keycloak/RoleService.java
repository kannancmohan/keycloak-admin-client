package com.kcm.test.service.keycloak;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
  private final Keycloak keycloak;
  @Value("${keycloak.realm}")
  private String realm;

  public void create(final String name) {
    var role = new RoleRepresentation();
    role.setName(name);
    keycloak.realm(realm).roles().create(role);
  }

  public List<RoleRepresentation> findAll() {
    return keycloak.realm(realm).roles().list();
  }

  public RoleRepresentation findByName(final String roleName) {
    return keycloak.realm(realm).roles().get(roleName).toRepresentation();
  }
}
