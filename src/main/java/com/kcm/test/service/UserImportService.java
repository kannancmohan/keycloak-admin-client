package com.kcm.test.service;

import static org.springframework.util.CollectionUtils.isEmpty;

import com.kcm.test.service.jpa.UserReadOnlyRepository;
import com.kcm.test.service.keycloak.KeycloakPartialImportService;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserImportService {
  private final UserReadOnlyRepository userReadOnlyRepository;
  private final ConversionService conversionService;
  private final KeycloakPartialImportService partialImportService;

  public Response importUsers(final AccountType accountType) {
    final var users = userReadOnlyRepository.findByAccountType(accountType.name());
    if (isEmpty(users)) {
      log.warn("No users found in db for accountType:{}", accountType);
      return null;
    } else {
      log.info("Found {} users for accountType:{}", users.size(), accountType);
      final var userRepresentations =
          users.stream()
              .map(user -> conversionService.convert(user, UserRepresentation.class))
              .collect(Collectors.toList());
      log.info(
          "No of users after converting to keycloak user type: {}", userRepresentations.size());
      final var importRepresentation = new PartialImportRepresentation();
      importRepresentation.setUsers(userRepresentations);
      return partialImportService.importUsers(importRepresentation);
    }
  }
}
