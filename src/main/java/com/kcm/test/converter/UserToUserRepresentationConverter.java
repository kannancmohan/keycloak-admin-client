package com.kcm.test.converter;

import static org.springframework.util.CollectionUtils.isEmpty;

import com.kcm.test.service.jpa.entity.User;
import com.kcm.test.service.jpa.entity.UserRole;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.core.convert.converter.Converter;

public class UserToUserRepresentationConverter implements Converter<User, UserRepresentation> {

  @Override
  public UserRepresentation convert(User source) {
    return prepareUserRepresentation(source);
  }

  private UserRepresentation prepareUserRepresentation(final User source) {
    // TODO check if password is present, else throw error
    final var password =
        prepareHashedPasswordRepresentation(source.getHashedPassword(), null, "sha1-salted", 1);
    final var newUser = new UserRepresentation();
    final var roles = source.getRoles();
    if (!isEmpty(roles)) {
      newUser.setRealmRoles(roles.stream().map(UserRole::getName).toList());
    }
    newUser.setUsername(source.getLogin());
    newUser.setCredentials(List.of(password));
    newUser.setEnabled(true);
    newUser.setEmailVerified(true);
    newUser.setCreatedTimestamp(Timestamp.from(Instant.now()).getTime());
    return newUser;
  }

  private CredentialRepresentation prepareHashedPasswordRepresentation(
      final String encodedPassword,
      final String passwordSalt,
      final String passwordHashingAlgorithm,
      final Integer passwordHashingIterations) {
    var hashingIterations = passwordHashingIterations == null ? 0 : passwordHashingIterations;
    var saltBytes =
        passwordSalt == null ? new byte[0] : passwordSalt.getBytes(StandardCharsets.UTF_8);
    final var credential = new CredentialRepresentation();
    credential.setTemporary(false);
    credential.setType(CredentialRepresentation.PASSWORD);

    final PasswordCredentialModel credentialModel =
        PasswordCredentialModel.createFromValues(
            passwordHashingAlgorithm, saltBytes, hashingIterations, encodedPassword);
    final String credentialData = credentialModel.getCredentialData();
    final String secretData = credentialModel.getSecretData();
    credential.setCredentialData(credentialData);
    credential.setSecretData(secretData);
    return credential;
  }
}
