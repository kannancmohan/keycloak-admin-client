package com.kcm.test.controller;

import com.kcm.test.model.UserRequest;
import com.kcm.test.service.RoleService;
import com.kcm.test.service.UserService;
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

  private final UserService userService;
  private final RoleService roleService;

  @GetMapping
  public List<UserRepresentation> findAll() {
    return userService.findAll();
  }

  @GetMapping("/{id}")
  public UserRepresentation findById(@PathVariable String id) {
    return userService.findById(id);
  }

  @GetMapping("/username/{username}")
  public List<UserRepresentation> findByUsername(@PathVariable String username) {
    return userService.findByUsername(username);
  }

  @PostMapping
  public ResponseEntity<URI> create(@RequestBody UserRequest userRequest) {
    var response = userService.create(userRequest);
    if (response.getStatus() != 201) {
      throw new RuntimeException("User was not created");
    }
    return ResponseEntity.created(response.getLocation()).build();
  }

  @PostMapping("/{userId}/group/{groupId}")
  public void assignToGroup(@PathVariable String userId, @PathVariable String groupId) {
    userService.assignToGroup(userId, groupId);
  }

  @PostMapping("/{userId}/role/{roleName}")
  public void assignRole(@PathVariable String userId, @PathVariable String roleName) {
    var role = roleService.findByName(roleName);
    userService.assignRole(userId, role);
  }
}
