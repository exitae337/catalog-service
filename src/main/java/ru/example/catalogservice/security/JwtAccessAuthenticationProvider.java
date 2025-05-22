package ru.example.catalogservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAccessAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenExtractor jwtTokenExtractor;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        UserDetailsImpl userDetails = new UserDetailsImpl(
                jwtTokenExtractor.extractUserId(token),
                jwtTokenExtractor.extractRole(token)
        );
        return new JwtAccessAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAccessAuthenticationToken.class);
    }
}
