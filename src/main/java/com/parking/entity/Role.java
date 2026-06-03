package com.parking.entity;

public enum Role {
    ADMIN,
    ATTENDANT;

    public String getAuthority() {
        return "ROLE_" + name();
    }
}
