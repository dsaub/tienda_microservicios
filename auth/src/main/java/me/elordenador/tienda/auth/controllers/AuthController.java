package me.elordenador.tienda.auth.controllers;

import me.elordenador.tienda.auth.dtos.TokenDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RestController
@RequestMapping("/auth")
class AuthController {
    @PostMapping("/login")
    public TokenDTO login(@RequestBody Map<String,String> body) {
        return new TokenDTO("mocked-data", "Bearer");
    }
}
