package be.syntra.java.advanced.knittingapitest.controller.api;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class MemberApiTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    protected void initRestTemplate(String user, String password) {
        restTemplate = restTemplate.withBasicAuth(user, password);
    }
}
