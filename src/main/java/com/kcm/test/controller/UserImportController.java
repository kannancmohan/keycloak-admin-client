package com.kcm.test.controller;

import com.kcm.test.service.AccountType;
import com.kcm.test.service.UserImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<String> importUsers(@PathVariable("accountType") AccountType accountType) {
    final var response = userImportService.importUsers(accountType);
    if (response == null || response.getStatus() != 200) {
      throw new RuntimeException("User's was not imported");
    }
    return ResponseEntity.ok("Users imported successfully.");
  }
}
