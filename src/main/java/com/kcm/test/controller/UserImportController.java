package com.kcm.test.controller;

import com.kcm.test.service.AccountType;
import com.kcm.test.service.UserImportService;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class UserImportController {

  private final UserImportService userImportService;

  @GetMapping("/type/{accountType}")
  public List<UserRepresentation> importUsers(
      @PathVariable("accountType") AccountType accountType) {
    userImportService.importUsers(accountType);
    return Collections.emptyList();
  }
}
