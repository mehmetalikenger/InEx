package com.stage.inex.webLayerTest;

import com.stage.inex.application.service.UserRegistrationService;
import com.stage.inex.presentation.controller.UserRegistrationController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

@WebMvcTest(UserRegistrationController.class)
@AutoConfigureRestTestClient
public class UserRegistrationControllerTests {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private UserRegistrationService userRegistrationService;

    @Test
    @Tag(value = "failure")
    void shouldThrowWhenNameIsBlank(){

        restTestClient.post()
                .uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                              {
                                "name": "",
                                "surname": "kenger",
                                "email": "test@email.com",
                                "password": "Zxcv123.4"
                              }
                        """
                )
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.name.message").isEqualTo("Name cannot be blank.");
    }

    @Test
    @Tag(value = "failure")
    void shouldThrowWhenEmailIsBlank(){

        restTestClient.post()
                .uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                              {
                                "name": "mali",
                                "surname": "kenger",
                                "email": "",
                                "password": "Zxcv123.4"
                              }
                        """
                )
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.email.message").isEqualTo("Email cannot be blank.");
    }

    @Test
    @Tag(value = "failure")
    void shouldThrowWhenPasswordIsBlank(){

        restTestClient.post()
                .uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                              {
                                "name": "mali",
                                "surname": "kenger",
                                "email": "test@email.com",
                                "password": ""
                              }
                        """
                )
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.password.message").isEqualTo("Password should be minimum 8 characters long. " +
                        "Password should contain at least one lowercase, " +
                        "one uppercase, one number and one special character. " +
                        "Allowed special characters are (!@#%^&*._+)");
    }

    @Test
    @Tag(value = "failure")
    void shouldThrowWhenEmailIsNotValid(){

        restTestClient.post()
                .uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                              {
                                "name": "mali",
                                "surname": "kenger",
                                "email": "test",
                                "password": "Zxcv123.4"
                              }
                        """
                )
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.email.message").isEqualTo("Email is not valid.");
    }

    @Test
    @Tag(value = "failure")
    void shouldThrowWhenPasswordIsShort(){

        restTestClient.post()
                .uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                              {
                                "name": "mali",
                                "surname": "kenger",
                                "email": "test@email.com",
                                "password": "Zxcv"
                              }
                        """
                )
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.password.message").isEqualTo("Password should be minimum 8 characters long. " +
                        "Password should contain at least one lowercase, " +
                        "one uppercase, one number and one special character. " +
                        "Allowed special characters are (!@#%^&*._+)");
    }

    @Test
    @Tag(value = "failure")
    void shouldThrowWhenPasswordDoNotHaveSpecialChar(){

        restTestClient.post()
                .uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                              {
                                "name": "mali",
                                "surname": "kenger",
                                "email": "test@email.com",
                                "password": "Zxcv1234"
                              }
                        """
                )
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.password.message").isEqualTo("Password should be minimum 8 characters long. " +
                        "Password should contain at least one lowercase, " +
                        "one uppercase, one number and one special character. " +
                        "Allowed special characters are (!@#%^&*._+)");
    }
}
