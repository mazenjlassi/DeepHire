package com.deephire.Dto;

import com.deephire.Enums.Role;

public record SignUpDto(String firstName, String lastName, Role role, String login, char[] password) {
}
