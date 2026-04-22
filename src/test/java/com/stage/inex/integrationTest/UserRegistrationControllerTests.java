package com.stage.inex.integrationTest;

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
public class UserRegistrationControllerTests {

    @Autowired
    private RestTestClient restTestClient;

    @Test
    @Tag("failure")
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
}
