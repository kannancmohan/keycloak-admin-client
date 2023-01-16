package com.kcm.test.service;

import static com.kcm.test.service.UserServiceUtil.preparePasswordRepresentation;
import static com.kcm.test.service.UserServiceUtil.prepareUserRepresentation;

import com.kcm.test.model.UserRequest;
import java.util.List;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
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
    var credentialRepresentation = preparePasswordRepresentation(request.password());
    var user = prepareUserRepresentation(request, credentialRepresentation);
    return keycloak.realm(realm).users().create(user);
  }
}
