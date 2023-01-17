package com.kcm.test.model;

import java.util.List;

public record UserRequest(String username, UserPasswordRequest password, List<String> roles) {}
