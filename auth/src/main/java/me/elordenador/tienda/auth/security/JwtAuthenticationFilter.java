package me.elordenador.tienda.auth.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final PasswordVersionCacheService passwordVersionCache;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenSyntaxAndSignatureValid(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Claims claims = jwtService.extractAllClaims(token);
        Long userId = claims.get("uid", Long.class);
        Integer tokenPwdVer = claims.get("pwd_ver", Integer.class);
        int currentPwdVer = passwordVersionCache.getCurrentVersion(userId);
        if (tokenPwdVer == null || currentPwdVer == -1 || !tokenPwdVer.equals(currentPwdVer)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (Boolean.TRUE.equals(claims.get("admin", Boolean.class))) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (Boolean.TRUE.equals(claims.get("seller", Boolean.class))) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        }

        AuthenticatedUser principal = new AuthenticatedUser(
                userId,
                claims.getSubject(),
                Boolean.TRUE.equals(claims.get("admin", Boolean.class)),
                Boolean.TRUE.equals(claims.get("seller", Boolean.class))
        );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
