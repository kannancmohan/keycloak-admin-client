package com.kcm.test.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {
  private final Keycloak keycloak;
  @Value("${keycloak.realm}")
  private String realm;

  public void create(final String name) {
    var group = new GroupRepresentation();
    group.setName(name);
    keycloak
        .realm(realm)
        .groups()
        .add(group);
  }

  public List<GroupRepresentation> findAll(){
    return keycloak
        .realm(realm)
        .groups()
        .groups();
  }

}
