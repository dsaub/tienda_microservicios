package me.elordenador.tienda.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import me.elordenador.tienda.auth.exception.ErrorCode.Codes;

/**
 * DTO para la petición de registro de un nuevo usuario
 * <p>
 *     En caso de fallo de validación, la API responde con un estado {@code 400 Bad Request}
 *     incluyendo los siguientes códigos de internacionalización (i18n) por campo:
 * </p>
 * <p>
 * @param full_name Nombre completo del usuario.
 *                  <ul>
 *                  <li>{@code REQUIRED_FULLNAME}: El campo esta vacio o es nulo</li>
 *                  </ul>
 * @param username Nombre de usuario único en la tienda.
 *                 <ul>
 *                 <li>{@code REQUIRED_USERNAME}: El campo esta vacio o es nulo</li>
 *                 <li>{@code INVALID_USERNAME_LENGTH}: No tiene entre 3 y 30 caracteres</li>
 *                 </ul>
 * @param email Dirección de correo electrónico
 *              <ul>
 *              <li>{@code REQUIRED_EMAIL}: El campo está vacio o es nulo.</li>
 *              <li>{@code INVALID_EMAIL}: El formato de correo no es valido</li>
 *              </ul>
 * @param password Contraseña en texto plano.
 *                 <ul>
 *                 <li>{@code REQUIRED_PASSWORD}: El campo esta vacio o es nulo</li>
 *                 <li>{@code PASSWORD_TOO_SHORT}: Tiene menos de 8 o mas de 64 caracteres</li>
 *                 <li>{@code WEAK_PASSWORD}: La contraseña es debil</li>
 *                 </ul>
 * </p>
 */
public record RegisterRequest(
        @NotBlank(message = Codes.REQUIRED_FULLNAME)
        String full_name,

        @NotBlank(message = Codes.REQUIRED_USERNAME)
        @Size(min = 3, max = 30, message = Codes.INVALID_USERNAME_LENGTH)
        String username,

        @NotBlank(message = Codes.REQUIRED_EMAIL)
        @Email(message = Codes.INVALID_EMAIL)
        String email,

        @NotBlank(message = Codes.REQUIRED_PASSWORD)
        @Size(min = 8, max = 64, message = Codes.PASSWORD_TOO_SHORT)
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$", message = Codes.WEAK_PASSWORD)
        String password
) {
}
