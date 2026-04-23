package com.stage.inex.integrationTest;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest()
@AutoConfigureRestTestClient
public class LoginTests {

    @Autowired
    RestTestClient restTestClient;

    Logger logger = LoggerFactory.getLogger(LoginTests.class);

    @Test
    @Tag(value = "failure")
    void throwWhenUserNotFound(){

        restTestClient.post()
                .uri("/user/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                            {
                                "email": "jane@email.com",
                                "password": "Zxcv123.4"
                            }
                        """
                )
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.email.message").isEqualTo("User not found.");
    }

    @Test
    @Tag(value = "failure")
    void throwWhenPasswordsDoNotMatch(){

        restTestClient.post()
                .uri("/user/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                            {
                                "email": "test@email.com",
                                "password": "Zxcv1234."
                            }
                        """
                )
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.password.message").isEqualTo("Passwords do not match.");
    }

    @Test
    @Tag(value = "success")
    void shouldAuthenticateUserSuccessfully(){

        restTestClient.post()
                .uri("/user/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                            {
                                "email": "test@email.com",
                                "password": "Zxcv123.4"
                            }
                        """
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectCookie()
                .httpOnly("accessToken", true)
                .expectCookie()
                .httpOnly("refreshToken", true);
    }
}
