package me.elordenador.tienda.auth.controllers;

import lombok.RequiredArgsConstructor;
import me.elordenador.tienda.auth.annotations.PublicEndpoint;
import me.elordenador.tienda.auth.exception.ApiException;
import me.elordenador.tienda.auth.exception.ErrorCode;
import me.elordenador.tienda.auth.model.User;
import me.elordenador.tienda.auth.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RestController
@RequestMapping("/api/internal")
@RequiredArgsConstructor
public class InternalController {
    private final UserRepository userRepository;
    @PublicEndpoint
    @GetMapping("/getUser/by-email")
    public User getUserByEmail(@RequestParam String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND);
        }
        return user.get();
    }
    @PublicEndpoint
    @GetMapping("/getUser/by-id")
    public User getUserById(@RequestParam Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND);
        }
        return user.get();
    }
    @PublicEndpoint
    @GetMapping("/getUser/by-username")
    public User getUserByUsername(@RequestParam String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND);
        }
        return user.get();
    }
}
