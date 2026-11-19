package me.elordenador.tienda.auth.security;

public record AuthenticatedUser(
        Long id,
        String email,
        boolean admin,
        boolean seller
) {
}
