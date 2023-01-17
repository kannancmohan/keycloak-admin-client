package com.kcm.test.converter;

import static org.springframework.util.CollectionUtils.isEmpty;

import com.kcm.test.model.UserRequest;
import java.util.List;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class UserRequestToUserRepresentationConverter
    implements Converter<UserRequest, UserRepresentation> {

  private final ConversionService conversionService;

  public UserRequestToUserRepresentationConverter(final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public UserRepresentation convert(final UserRequest source) {
    var credentialRepresentation =
        conversionService.convert(source.password(), CredentialRepresentation.class);
    return prepareUserRepresentation(source, credentialRepresentation);
  }

  private UserRepresentation prepareUserRepresentation(
      final UserRequest request, final CredentialRepresentation cR) {
    final var newUser = new UserRepresentation();
    newUser.setUsername(request.username());
    newUser.setCredentials(List.of(cR));
    newUser.setEnabled(true);
    newUser.setEmailVerified(true);
    final var roles = request.roles();
    if (!isEmpty(roles)) {
      newUser.setRealmRoles(roles);
    }
    return newUser;
  }
}
