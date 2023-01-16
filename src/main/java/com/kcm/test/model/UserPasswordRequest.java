package com.kcm.test.model;

public record UserPasswordRequest(
    String password,
    boolean isEncoded,
    String encodedAlgorithm,
    String hashedSaltValue,
    Integer hashIterations) {}
