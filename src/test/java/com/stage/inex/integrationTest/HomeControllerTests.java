package com.stage.inex.integrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest
@AutoConfigureRestTestClient
public class HomeControllerTests {

    @Autowired
    private RestTestClient restTestClient;

    @Test
    void shouldReturnDefaultMessage(){

        restTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk();
    }
}


