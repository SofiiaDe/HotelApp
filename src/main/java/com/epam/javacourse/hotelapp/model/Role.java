package com.epam.javacourse.hotelapp.model;

import java.io.Serializable;

public enum Role implements Serializable {

    MANAGER("manager"),
    CLIENT("client");

    private static final long serialVersionUID = 5512347890045634L;


    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static Role getRoleByType(String type) {
        for (Role role : values()) {
            if (role.getRole().equals(type)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role found with such input data: [" + type + "]");
    }
}
