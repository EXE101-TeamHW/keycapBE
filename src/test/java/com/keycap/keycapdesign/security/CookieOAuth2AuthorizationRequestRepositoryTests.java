package com.keycap.keycapdesign.security;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CookieOAuth2AuthorizationRequestRepositoryTests {

    private final CookieOAuth2AuthorizationRequestRepository repository =
            new CookieOAuth2AuthorizationRequestRepository("test-signing-secret");

    @Test
    void savesLoadsAndRemovesAuthorizationRequestWithoutSession() {
        OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .clientId("client-id")
                .redirectUri("https://backend.example.com/login/oauth2/code/google")
                .state("state-value")
                .build();

        MockHttpServletRequest saveRequest = new MockHttpServletRequest();
        saveRequest.setSecure(true);
        MockHttpServletResponse saveResponse = new MockHttpServletResponse();
        repository.saveAuthorizationRequest(authorizationRequest, saveRequest, saveResponse);

        Cookie savedCookie = saveResponse.getCookie("oauth2_auth_request");
        assertNotNull(savedCookie);
        assertNull(saveRequest.getSession(false));

        MockHttpServletRequest callbackRequest = new MockHttpServletRequest();
        callbackRequest.setSecure(true);
        callbackRequest.setCookies(savedCookie);
        MockHttpServletResponse callbackResponse = new MockHttpServletResponse();

        OAuth2AuthorizationRequest loaded =
                repository.removeAuthorizationRequest(callbackRequest, callbackResponse);

        assertNotNull(loaded);
        assertEquals("state-value", loaded.getState());
        assertEquals("client-id", loaded.getClientId());
        assertNotNull(callbackResponse.getCookie("oauth2_auth_request"));
    }
}
