package com.snm.security.dto;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class AuthResponse implements Serializable {

    @Nullable
    private final String token;
    @Nullable
    private final String message;

    private boolean isNewUser;
    private boolean isUserAccepted;
    private boolean isAdmin;

    public static AuthResponse success(@Nullable String token, boolean isNewUser, boolean isUserAccepted) {
        return new AuthResponse(token, null, isNewUser, isUserAccepted);
    }

    public static AuthResponse success(@Nullable String token, boolean isNewUser, boolean isUserAccepted, boolean isAdmin) {
        return new AuthResponse(token, null, isNewUser, isUserAccepted, isAdmin);
    }

    public static AuthResponse success(@Nullable String token, @Nullable String message) {
        return new AuthResponse(token, message);
    }

    public static AuthResponse fail(@Nullable String message) {
        return new AuthResponse(null, message);
    }

    private AuthResponse(@Nullable String token, @Nullable String message) {
        this.token = token;
        this.message = message;
    }

    private AuthResponse(@Nullable String token, @Nullable String message, boolean isNewUser, boolean isUserAccepted) {
        this.token = token;
        this.message = message;
        this.isNewUser = isNewUser;
        this.isUserAccepted = isUserAccepted;
    }

    public AuthResponse(@Nullable String token, @Nullable String message, boolean isNewUser, boolean isUserAccepted, boolean isAdmin) {
        this.token = token;
        this.message = message;
        this.isNewUser = isNewUser;
        this.isUserAccepted = isUserAccepted;
        this.isAdmin = isAdmin;
    }

    @Nullable
    public String getToken() {
        return token;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public boolean isUserAccepted() {
        return isUserAccepted;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
