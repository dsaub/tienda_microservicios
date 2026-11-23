package me.elordenador.tienda.auth.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.elordenador.tienda.auth.annotations.PublicEndpoint;
import me.elordenador.tienda.auth.dtos.RegisterRequest;
import me.elordenador.tienda.auth.dtos.RegisterStatus;
import me.elordenador.tienda.auth.dtos.TokenDTO;
import me.elordenador.tienda.auth.dtos.UserLoginDTO;
import me.elordenador.tienda.auth.exception.ApiException;
import me.elordenador.tienda.auth.exception.ErrorCode;
import me.elordenador.tienda.auth.model.User;
import me.elordenador.tienda.auth.repository.UserRepository;
import me.elordenador.tienda.auth.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador de endpoints PUBLICO de Autenticación
 * @author Daniel
 * @version 1.0
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Metodo para iniciar sesión
     * @param body Datos de inicio de sesión, se espera:
     *             {@snippet lang="json" :
     *             {
     *              "email": "prueba_email@token.net",
     *              "password": "contranosegura"
     *             }
     *             }
     * @return Objeto TokenDTO con tu token.
     */
    @PostMapping("/login")
    @PublicEndpoint
    public TokenDTO login(@RequestBody UserLoginDTO body) {
        User user = userRepository.findByEmail(body.email()).orElse(null);

        if (user == null || !passwordEncoder.matches(body.password(), user.getPassword())) {
            throw new ApiException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtService.generateToken(user);
        return TokenDTO.builder().type("Bearer").token(token).build();
    }

    @PublicEndpoint
    @PostMapping("/register")
    public RegisterStatus register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ApiException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new ApiException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password());
        user.setEmail(request.email());
        user.setFullname(request.full_name());
        user.setEnabled(true);
        userRepository.save(user);
        return new RegisterStatus(user.getUsername(), "ACTIVE");
    }

}
