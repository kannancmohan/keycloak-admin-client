package com.kcm.test.service;

import static org.springframework.util.CollectionUtils.isEmpty;

import com.kcm.test.service.jpa.UserReadOnlyRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserImportService {
  private final UserReadOnlyRepository userReadOnlyRepository;
  private final ConversionService conversionService;

  public void importUsers(final AccountType accountType) {
    final var users = userReadOnlyRepository.findByAccountType(accountType.name());
    if (isEmpty(users)) {
      log.warn("No users found in db for accountType:{}", accountType);
    } else {
      log.info("Found {} users for accountType:{}", users.size(), accountType);
      final var userRepresentations =
          users.stream()
              .map(user -> conversionService.convert(user, UserRepresentation.class))
              .collect(Collectors.toSet());
      log.info(
          "No of users after converting to keycloak user type: {}", userRepresentations.size());
    }
  }
}
