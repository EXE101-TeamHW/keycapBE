package com.keycap.keycapdesign.security;

import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.AuthProvider;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.enums.UserStatus;
import com.keycap.keycapdesign.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final String frontendLoginUrl;

    public OAuth2SuccessHandler(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder,
            @Value("${app.frontend-url:http://localhost:5173}") String frontendUrl) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.frontendLoginUrl = frontendUrl.replaceAll("/+$", "") + "/login";
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");
        String name = (String) oAuth2User.getAttributes().get("name");
        String sub = (String) oAuth2User.getAttributes().get("sub");

        User user = userRepository.findByProviderAndProviderId(AuthProvider.GOOGLE, sub)
                .orElseGet(() -> userRepository.findByEmail(email).orElse(null));
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setFullName(name);
            user.setPassword(passwordEncoder.encode("oauth2-" + sub));
            user.setRole(Role.CUSTOMER);
            user.setStatus(UserStatus.ACTIVE);
        } else if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode("oauth2-" + sub));
        }
        user.setProvider(AuthProvider.GOOGLE);
        user.setProviderId(sub);
        user.setEmailVerified(true);
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        String redirectUrl = frontendLoginUrl
                + "?token=" + encode(token)
                + "&userId=" + encode(user.getId())
                + "&role=" + encode(user.getRole().name());

        response.sendRedirect(redirectUrl);
    }

    private String encode(Object value) {
        return URLEncoder.encode(String.valueOf(value), StandardCharsets.UTF_8);
    }
}

