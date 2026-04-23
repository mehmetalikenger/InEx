package com.stage.inex.webLayerTest;

import com.stage.inex.application.service.UserAuthService;
import com.stage.inex.presentation.controller.UserAuthController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

@WebMvcTest(UserAuthController.class)
@AutoConfigureRestTestClient
public class UserAuthControllerTests {

    @Autowired
    RestTestClient restTestClient;

    @MockitoBean
    private UserAuthService userAuthService;

    @Test
    @Tag(value = "failure")
    void shouldThrowWhenEmailIsBlank(){

        restTestClient.post()
                .uri("/user/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                        {
                            "email":"",
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
                .uri("/user/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                        {
                            "email":"test@email.com",
                            "password": ""
                        }
                        
                        """
                )
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.password.message").isEqualTo("Password cannot be blank.");
    }

    @Test
    @Tag(value = "failure")
    void shouldThrowWhenEmailIsNotValid(){

        restTestClient.post()
                .uri("/user/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                              {
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
}
