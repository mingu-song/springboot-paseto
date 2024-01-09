package mingu.spring.paseto.dto;

import jakarta.validation.constraints.NotNull;

public record LoginDTO(
        @NotNull String username,
        @NotNull String password
) {}
