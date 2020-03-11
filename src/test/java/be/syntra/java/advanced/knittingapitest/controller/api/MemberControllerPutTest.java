package be.syntra.java.advanced.knittingapitest.controller.api;

import be.syntra.java.advanced.knittingapitest.dto.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Base64;

import static be.syntra.java.advanced.knittingapitest.util.TestHelper.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerPutTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private long existingMemberId;

    @BeforeEach
    void setup() {
        givenAtLeastOneMemberExists();
        existingMemberId = anExistingId();
    }

    /**
     * Test that the backend returns a 401 UNAUTHORIZED HttpStatus
     * when we are not authenticated
     */
    @Test
    void givenNotAuthenticated_whenPutMember_thenShouldReturnUnauthorized() {
        // given
        Member member = aMember();
        member.setId(existingMemberId);

        // when
        ResponseEntity<String> response = restTemplate.exchange(
                RESOURCE_URL + "/" + existingMemberId,
                HttpMethod.PUT,
                new HttpEntity<>(member),
                String.class);

        // then
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Test that the backend returns a 403 FORBIDDEN HttpStatus
     * when we are authenticated, but not authorized to PUT a member
     */
    @Test
    void givenAuthenticatedAsUser_whenPutMember_thenShouldReturnForbidden() {
        // given
        Member member = aMember();
        member.setId(existingMemberId);

        // when
        ResponseEntity<String> response = restTemplate.exchange(
                RESOURCE_URL + "/" + existingMemberId,
                HttpMethod.PUT,
                new HttpEntity<>(
                        member,
                        createBasicAuthHeader("user", "password")
                ),
                String.class);

        // then
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    /**
     * Test that the backend returns a 204 NO CONTENT HttpStatus
     * when we are authenticated and authorized to PUT a member
     */
    @Test
    void givenAuthenticatedAsSuperAdmin_whenPutMember_thenShouldReturnNoContent() {
        // given
        Member member = aMember();
        member.setId(existingMemberId);

        // when
        ResponseEntity<String> response = restTemplate.exchange(
                RESOURCE_URL + "/" + existingMemberId,
                HttpMethod.PUT,
                new HttpEntity<>(
                        member,
                        createBasicAuthHeader("super-admin", "super-admin")
                ),
                String.class);

        // then
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    /**
     * Test that the backend returns a 400 BAD REQUEST HttpStatus
     * when we put a member with invalid details (all fields are required)
     *
     * Try using validation annotations on the controller/DTO (@NotNull, @Valid...)
     * Spring should handle validation and return 400 when the DTO doesn't validate
     * Similar to form validation!
     */
    @Test
    void givenAuthenticatedAsSuperAdmin_whenPutInvalidMember_thenShouldReturnBadRequest() {
        // given
        Member member = anInvalidMember();
        member.setId(existingMemberId);

        // when
        ResponseEntity<String> response = restTemplate.exchange(
                RESOURCE_URL + "/" + existingMemberId,
                HttpMethod.PUT,
                new HttpEntity<>(
                        member,
                        createBasicAuthHeader("super-admin", "super-admin")
                ),
                String.class);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test that the backend returns a 400 BAD REQUEST HttpStatus
     * when we put a member with valid details but non-matching id ( id field != /{id} )
     */
    @Test
    void givenAuthenticatedAsSuperAdmin_whenPutMemberWithIncorrectId_thenShouldReturnBadRequest() {
        // given
        Member member = aMember();
        member.setId(existingMemberId + 1);

        // when
        ResponseEntity<String> response = restTemplate.exchange(
                RESOURCE_URL + "/" + existingMemberId,
                HttpMethod.PUT,
                new HttpEntity<>(
                        member,
                        createBasicAuthHeader("super-admin", "super-admin")
                ),
                String.class);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test that the backend returns a 404 NOT FOUND HttpStatus
     * when we put a member to a non existing endpoint ({id} doesn't exist)
     */
    @Test
    void givenAuthenticatedAsSuperAdmin_whenPutMemberOnNonExistingId_thenShouldReturnNotFound() {
        // given
        final long nonExistingMemberId = Long.MAX_VALUE;
        Member member = aMember();
        member.setId(nonExistingMemberId);

        // when
        ResponseEntity<String> response = restTemplate.exchange(
                RESOURCE_URL + "/" + nonExistingMemberId,
                HttpMethod.PUT,
                new HttpEntity<>(
                        member,
                        createBasicAuthHeader("super-admin", "super-admin")
                ),
                String.class);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private HttpHeaders createBasicAuthHeader(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            String encoded = Base64.getEncoder().encodeToString(auth.getBytes());
            String authHeader = "Basic " + encoded;
            set("Authorization", authHeader);
        }};
    }

}
