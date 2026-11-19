package me.elordenador.tienda.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

// No, no es el Token Pirata del Fortnite.

/**
 * DTO para el token de inicio de sesión (aka. No es el token pirata)
 */
@Data
@AllArgsConstructor
@Builder
public class TokenDTO {
    /**
     * El token
     */
    private String token;
    /**
     * Tipo del token (usualmente Bearer)
     */
    private String type;
}
