package be.syntra.java.advanced.knittingapitest.controller.api;

import be.syntra.java.advanced.knittingapitest.dto.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static be.syntra.java.advanced.knittingapitest.util.TestHelper.*;

public class MemberControllerPostTest extends MemberApiTest {

    /**
     * Test that the backend returns a 401 UNAUTHORIZED HttpStatus
     * when we are not authenticated
     */
    @Test
    void givenNotAuthenticated_whenPostMember_thenShouldReturnUnauthorized() {
        // given
        Member member = aMember();

        // when
        ResponseEntity<?> response = restTemplate.postForEntity(RESOURCE_URL, member, null);

        // then
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Test that the backend returns a 403 FORBIDDEN HttpStatus
     * when we are authenticated, but not authorized to POST a member
     */
    @Test
    void givenAuthenticatedAsUser_whenPostMember_thenShouldReturnForbidden() {
        // given
        initRestTemplate("user", "password");
        Member member = aMember();

        // when
        ResponseEntity<?> response = restTemplate.postForEntity(RESOURCE_URL, member, null);

        // then
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }


    /**
     * Test that the backend returns a 201 CREATED HttpStatus
     * when we are authenticated and authorized to POST a member
     */
    @Test
    void givenAuthenticatedAsAdmin_whenPostMember_thenShouldReturnCreated() {
        // given
        initRestTemplate("admin", "admin");
        Member member = aMember();

        // when
        ResponseEntity<?> response = restTemplate.postForEntity(RESOURCE_URL, member, null);

        // then
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    /**
     * Test that the backend returns a 400 BAD REQUEST HttpStatus
     * when we post a member with invalid details (all fields are required, except id)
     *
     * Try using validation annotations on the controller/DTO (@NotNull, @Valid...)
     * Spring should handle validation and return 400 when the DTO doesn't validate
     * Similar to form validation!
     */
    @Test
    void givenAuthenticatedAsAdmin_whenPostInvalidMember_thenShouldReturnBadRequest() {
        // given
        initRestTemplate("admin", "admin");
        Member member = anInvalidMember();

        // when
        ResponseEntity<?> response = restTemplate.postForEntity(RESOURCE_URL, member, null);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
