package com.kcm.test.service.keycloak;

import com.kcm.test.model.UserPasswordRequest;
import com.kcm.test.model.UserRequest;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

public final class UserServiceUtil {
  private UserServiceUtil() {
    throw new UnsupportedOperationException();
  }

  public static UserRepresentation prepareUserRepresentation(
      final UserRequest request, final CredentialRepresentation cR) {
    final var newUser = new UserRepresentation();
    newUser.setUsername(request.username());
    newUser.setCredentials(List.of(cR));
    newUser.setEnabled(true);
    return newUser;
  }

  public static CredentialRepresentation preparePasswordRepresentation(
      final UserPasswordRequest request) {
    Objects.requireNonNull(request);
    Objects.requireNonNull(request.password());
    if (request.isEncoded() && request.encodedAlgorithm() != null) {
      return prepareHashedPasswordRepresentation(
          request.password(),
          request.hashedSaltValue(),
          request.encodedAlgorithm(),
          request.hashIterations());
    }
    final var cR = new CredentialRepresentation();
    cR.setTemporary(false);
    cR.setType(CredentialRepresentation.PASSWORD);
    cR.setValue(request.password());
    return cR;
  }

  @SuppressWarnings("unused")
  private static CredentialRepresentation prepareHashedPasswordRepresentation2(
      final String password) throws IllegalAccessException, NoSuchFieldException {
    final var credential = new CredentialRepresentation();
    credential.setTemporary(false);
    credential.setType(CredentialRepresentation.PASSWORD);

    Field algorithm = credential.getClass().getDeclaredField("algorithm");
    algorithm.setAccessible(true);
    algorithm.set(credential, "sha1-salted");

    Field hashIterations = credential.getClass().getDeclaredField("hashIterations");
    hashIterations.setAccessible(true);
    hashIterations.set(credential, 1);

    Field hashedSaltedValue = credential.getClass().getDeclaredField("hashedSaltedValue");
    hashedSaltedValue.setAccessible(true);
    hashedSaltedValue.set(credential, password);

    return credential;
  }

  private static CredentialRepresentation prepareHashedPasswordRepresentation(
      final String encodedPassword,
      final String passwordSalt,
      final String passwordHashingAlgorithm,
      Integer passwordHashingIterations) {
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
