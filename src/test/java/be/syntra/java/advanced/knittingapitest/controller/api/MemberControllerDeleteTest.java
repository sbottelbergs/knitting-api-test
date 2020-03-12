package be.syntra.java.advanced.knittingapitest.controller.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static be.syntra.java.advanced.knittingapitest.util.TestHelper.*;

public class MemberControllerDeleteTest extends MemberApiTest {

    /**
     * Test that the backend returns a 401 UNAUTHORIZED HttpStatus
     * when we are not authenticated
     */
    @Test
    void givenNotAuthenticated_whenDeleteMember_thenShouldReturnUnauthorized() {
        // given
        givenAtLeastOneMemberExists();

        // when
        ResponseEntity<String> response = restTemplate.exchange(
                RESOURCE_URL + "/" + anExistingId(),
                HttpMethod.DELETE,
                null,
                String.class
        );

        // then
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Test that the backend returns a 403 FORBIDDEN HttpStatus
     * when we are authenticated, but not authorized to DELETE a member
     */
    @Test
    void givenAuthenticatedAsUser_whenDeleteMember_thenShouldReturnForbidden() {
        // given
        givenAtLeastOneMemberExists();
        initRestTemplate("user", "password");

        // when
        ResponseEntity<String> response = restTemplate.exchange(
                RESOURCE_URL + "/" + anExistingId(),
                HttpMethod.DELETE,
                null,
                String.class
        );

        // then
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    /**
     * Test that the backend returns a 204 NO CONTENT HttpStatus
     * when we are authenticated and authorized to DELETE a member
     */
    @Test
    void givenAuthenticatedAsAdmin_whenDeleteExistingMember_thenShouldReturnNoContent() {
        // given
        givenAtLeastOneMemberExists();
        initRestTemplate("admin", "admin");

        // when
        ResponseEntity<String> response = restTemplate.exchange(
                RESOURCE_URL + "/" + anExistingId(),
                HttpMethod.DELETE,
                null,
                String.class
        );

        // then
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    /**
     * Test that the backend returns a 404 NOT FOUND HttpStatus
     * when we DELETE a non-existing member ({id} which doesn't exist)
     */
    @Test
    void givenAuthenticatedAsAdmin_whenDeleteNonExistingMember_thenShouldReturn404() {
        // given
        final long nonExistingId = Long.MAX_VALUE;
        initRestTemplate("admin", "admin");

        // when
        ResponseEntity<String> response = restTemplate.exchange(
                RESOURCE_URL + "/" + nonExistingId,
                HttpMethod.DELETE,
                null,
                String.class
        );

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
