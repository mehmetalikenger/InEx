package com.stage.inex.integrationTest;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;


@SpringBootTest
@AutoConfigureRestTestClient
@Transactional
public class RegistrationTests {

    @Autowired
    private RestTestClient restTestClient;

    @Test
    @Tag(value = "failure")
    void shouldThrowWhenEmailIsAlreadyTaken(){

        restTestClient.post()
                .uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                        {
                            "name": "john",
                            "surname": "doe",
                            "email": "test@email.com",
                            "password": "Zxcv123.4"
                        }
                        """
                )
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .jsonPath("$.email.message").isEqualTo("Email is already taken.");
    }


    @Test
    @Tag(value = "failure")
    void shouldThrowWhenPasswordIsNotValid(){

        restTestClient.post()
                .uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                            {
                                 "name": "john",
                                "surname": "doe",
                                "email": "test@email.com",
                                "password": "Zx234"
                            }
                        """
                )
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectBody()
                .jsonPath("$.password.message").isEqualTo("Password should be minimum 8 characters long. " +
                                                                                "Password should contain at least one lowercase, " +
                                                                                "one uppercase, one number and one special character. " +
                                                                                "Allowed special characters are (!@#%^&*._+)");
    }

    @Test
    @Tag(value = "success")
    void shouldRegisterUserSuccessfully(){

        restTestClient.post()
                .uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                            {
                                 "name": "jane",
                                "surname": "doe",
                                "email": "jane@email.com",
                                "password": "Zxcv123.4"
                            }
                        """
                )
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED);
    }
}
