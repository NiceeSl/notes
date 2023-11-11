package com.notes.notes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("testing")
public abstract class AbstractSpringBootTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected ObjectMapper objectMapper;
}
