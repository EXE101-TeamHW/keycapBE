package com.keycap.keycapdesign.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Component
public class CookieOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String COOKIE_NAME = "oauth2_auth_request";
    private static final Duration COOKIE_LIFETIME = Duration.ofMinutes(3);
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final byte[] signingKey;

    public CookieOAuth2AuthorizationRequestRepository(@Value("${jwt.secret:}") String jwtSecret) {
        this.signingKey = sha256(jwtSecret);
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        String cookieValue = readCookie(request);
        if (cookieValue == null) {
            return null;
        }

        try {
            String[] parts = cookieValue.split("\\.", 2);
            if (parts.length != 2) {
                return null;
            }

            byte[] payload = Base64.getUrlDecoder().decode(parts[0]);
            byte[] receivedSignature = Base64.getUrlDecoder().decode(parts[1]);
            if (!MessageDigest.isEqual(sign(payload), receivedSignature)) {
                return null;
            }

            return deserialize(payload);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        if (authorizationRequest == null) {
            deleteCookie(request, response);
            return;
        }

        byte[] payload = serialize(authorizationRequest);
        String value = Base64.getUrlEncoder().withoutPadding().encodeToString(payload)
                + "."
                + Base64.getUrlEncoder().withoutPadding().encodeToString(sign(payload));

        addCookie(response, value, COOKIE_LIFETIME, isSecure(request));
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
                                                                 HttpServletResponse response) {
        OAuth2AuthorizationRequest authorizationRequest = loadAuthorizationRequest(request);
        deleteCookie(request, response);
        return authorizationRequest;
    }

    private String readCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
        addCookie(response, "", Duration.ZERO, isSecure(request));
    }

    private void addCookie(HttpServletResponse response, String value, Duration maxAge, boolean secure) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, value)
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/")
                .maxAge(maxAge)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private boolean isSecure(HttpServletRequest request) {
        return request.isSecure()
                || "https".equalsIgnoreCase(request.getHeader("X-Forwarded-Proto"));
    }

    private byte[] sign(byte[] payload) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(signingKey, HMAC_ALGORITHM));
            return mac.doFinal(payload);
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot sign OAuth2 authorization request", exception);
        }
    }

    private byte[] serialize(OAuth2AuthorizationRequest authorizationRequest) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try (GZIPOutputStream gzip = new GZIPOutputStream(bytes);
                 ObjectOutputStream output = new ObjectOutputStream(gzip)) {
                output.writeObject(authorizationRequest);
            }
            return bytes.toByteArray();
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot serialize OAuth2 authorization request", exception);
        }
    }

    private OAuth2AuthorizationRequest deserialize(byte[] payload) throws Exception {
        try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(payload));
             ObjectInputStream input = new ObjectInputStream(gzip)) {
            return (OAuth2AuthorizationRequest) input.readObject();
        }
    }

    private static byte[] sha256(String value) {
        try {
            return MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot initialize OAuth2 cookie signing key", exception);
        }
    }
}
