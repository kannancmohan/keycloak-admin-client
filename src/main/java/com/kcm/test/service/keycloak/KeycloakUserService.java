package com.kcm.test.service.keycloak;

import com.kcm.test.model.UserRequest;
import java.util.List;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {

  private final Keycloak keycloak;
  private final ConversionService conversionService;

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
    var user = conversionService.convert(request, UserRepresentation.class);
    return keycloak.realm(realm).users().create(user);
  }
}
