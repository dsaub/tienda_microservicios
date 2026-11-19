package me.elordenador.tienda.auth.controllers;

import lombok.RequiredArgsConstructor;
import me.elordenador.tienda.auth.annotations.PublicEndpoint;
import me.elordenador.tienda.auth.dtos.TokenDTO;
import me.elordenador.tienda.auth.dtos.UserLoginDTO;
import me.elordenador.tienda.auth.exceptions.InvalidLoginException;
import me.elordenador.tienda.auth.model.User;
import me.elordenador.tienda.auth.repository.UserRepository;
import me.elordenador.tienda.auth.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/login")
    @PublicEndpoint
    public TokenDTO login(@RequestBody UserLoginDTO body) {
        User user = userRepository.findByEmail(body.email()).orElse(null);

        if (user == null || !passwordEncoder.matches(body.password(), user.getPassword())) {
            throw new InvalidLoginException();
        }

        String token = jwtService.generateToken(user);
        return TokenDTO.builder().type("Bearer").token(token).build();
    }
}
