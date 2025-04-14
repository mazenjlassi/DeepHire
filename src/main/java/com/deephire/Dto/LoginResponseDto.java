package com.deephire.Dto;

import lombok.Builder;

@Builder
public record LoginResponseDto(
        String token,
        UserDto user
) {}
