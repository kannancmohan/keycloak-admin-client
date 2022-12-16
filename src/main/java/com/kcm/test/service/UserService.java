package com.kcm.test.service;

import com.kcm.test.model.UserRequest;
import java.util.List;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final Keycloak keycloak;
  @Value("${keycloak.realm}")
  private String realm;

  public List<UserRepresentation> findAll() {
    return keycloak.realm(realm).users().list();
  }

  public List<UserRepresentation> findByUsername(final String username) {
    return keycloak.realm(realm).users().search(username);
  }

  public UserRepresentation findById(final String id) {
    return keycloak.realm(realm).users().get(id).toRepresentation();
  }

  public void assignToGroup(final String userId, final String groupId) {
    keycloak.realm(realm).users().get(userId).joinGroup(groupId);
  }

  public void assignRole(final String userId, final RoleRepresentation roleRepresentation) {
    keycloak.realm(realm).users().get(userId).roles().realmLevel().add(List.of(roleRepresentation));
  }

  public Response create(final UserRequest request) {
    var password = preparePasswordRepresentation(request.password());
    var user = prepareUserRepresentation(request, password);
    return keycloak.realm(realm).users().create(user);
  }

  public UserRepresentation prepareUserRepresentation(
      final UserRequest request, final CredentialRepresentation cR) {
    final var newUser = new UserRepresentation();
    newUser.setUsername(request.username());
    newUser.setCredentials(List.of(cR));
    newUser.setEnabled(true);
    return newUser;
  }

  public CredentialRepresentation preparePasswordRepresentation(final String password) {
    final var cR = new CredentialRepresentation();
    cR.setTemporary(false);
    cR.setType(CredentialRepresentation.PASSWORD);
    cR.setValue(password);
    return cR;
  }
}
