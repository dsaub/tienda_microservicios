package me.elordenador.tienda.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Formulario de inicio de sesion
 * @param email
 * @param password
 */
public record UserLoginDTO(
        @NotBlank @Email(message = "INVALID_EMAIL") String email,
        @NotBlank String password
) {}