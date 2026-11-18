package me.elordenador.tienda.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

// No, no es el Token Pirata del Fortnite.
@Data
@AllArgsConstructor
public class TokenDTO {
    private String token;
    private String type;
}
