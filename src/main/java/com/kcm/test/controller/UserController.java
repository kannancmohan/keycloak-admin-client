package com.kcm.test.controller;

import com.kcm.test.model.UserRequest;
import com.kcm.test.service.keycloak.KeycloakRoleService;
import com.kcm.test.service.keycloak.KeycloakUserService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final KeycloakUserService keycloakUserService;
  private final KeycloakRoleService keycloakRoleService;

  @GetMapping
  public List<UserRepresentation> findAll() {
    return keycloakUserService.findAll();
  }

  @GetMapping("/{id}")
  public UserRepresentation findById(@PathVariable String id) {
    return keycloakUserService.findById(id);
  }

  @GetMapping("/username/{username}")
  public List<UserRepresentation> findByUsername(@PathVariable String username) {
    return keycloakUserService.findByUsername(username);
  }

  @PostMapping
  public ResponseEntity<URI> create(@RequestBody UserRequest userRequest) {
    var response = keycloakUserService.create(userRequest);
    if (response.getStatus() != 201) {
      throw new RuntimeException("User was not created");
    }
    return ResponseEntity.created(response.getLocation()).build();
  }

  @PostMapping("/{userId}/group/{groupId}")
  public void assignToGroup(@PathVariable String userId, @PathVariable String groupId) {
    keycloakUserService.assignToGroup(userId, groupId);
  }

  @PostMapping("/{userId}/role/{roleName}")
  public void assignRole(@PathVariable String userId, @PathVariable String roleName) {
    var role = keycloakRoleService.findByName(roleName);
    keycloakUserService.assignRole(userId, role);
  }
}
