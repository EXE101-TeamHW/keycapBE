package com.keycap.keycapdesign.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2FailureHandler.class);

    private final String frontendLoginUrl;

    public OAuth2FailureHandler(@Value("${app.frontend-url:http://localhost:5173}") String frontendUrl) {
        this.frontendLoginUrl = frontendUrl.replaceAll("/+$", "") + "/login";
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String errorCode = exception instanceof OAuth2AuthenticationException oauthException
                ? oauthException.getError().getErrorCode()
                : "oauth2_authentication_failed";
        String diagnosticText = exception instanceof OAuth2AuthenticationException oauthException
                ? String.join(" ", String.valueOf(oauthException.getError().getDescription()),
                        String.valueOf(oauthException.getCause()))
                : String.valueOf(exception.getCause());
        diagnosticText = diagnosticText.toLowerCase(Locale.ROOT);

        if (diagnosticText.contains("invalid_client")) {
            errorCode = "oauth_client_invalid";
        } else if (diagnosticText.contains("invalid_grant")) {
            errorCode = "oauth_grant_invalid";
        } else if (diagnosticText.contains("redirect_uri_mismatch")) {
            errorCode = "oauth_redirect_uri_mismatch";
        }

        log.warn("Google OAuth2 authentication failed with code: {}", errorCode);
        response.sendRedirect(frontendLoginUrl + "?oauthError=" + encode(errorCode));
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
