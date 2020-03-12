package be.syntra.java.advanced.knittingapitest.controller.api;

import be.syntra.java.advanced.knittingapitest.dto.Address;
import be.syntra.java.advanced.knittingapitest.dto.Member;
import be.syntra.java.advanced.knittingapitest.dto.MemberList;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static be.syntra.java.advanced.knittingapitest.util.TestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class MemberControllerGetTest extends MemberApiTest {

    // GET /members

    /**
     * Test that the backend returns a 401 UNAUTHORIZED HttpStatus
     * when we are not authenticated
     */
    @Test
    void givenNotAuthenticated_whenGetMembers_thenShouldReturnUnauthorized() {
        // given

        // when
        ResponseEntity<MemberList> response = restTemplate.getForEntity(RESOURCE_URL, MemberList.class);

        // then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Test that the backend returns a MemberList with at least one member
     * when we are authenticated as user
     */
    @Test
    void givenAuthenticatedAsUser_whenGetMembers_thenShouldReturnMembers() {
        // given
        givenAtLeastOneMemberExists();
        initRestTemplate("user", "password");

        // when
        ResponseEntity<MemberList> response = restTemplate.getForEntity(RESOURCE_URL, MemberList.class);

        // then
        assertValidResponse(response);
        assertValidMemberList(response.getBody());
    }

    /**
     * Test that the backend returns a MemberList with at least one member
     * when we are authenticated as admin
     */
    @Test
    void givenAuthenticatedAsAdmin_whenGetMembers_thenShouldReturnMembers() {
        // given
        givenAtLeastOneMemberExists();
        initRestTemplate("admin", "admin");

        // when
        ResponseEntity<MemberList> response = restTemplate.getForEntity(RESOURCE_URL, MemberList.class);

        // then
        assertValidResponse(response);
        assertValidMemberList(response.getBody());
    }

    /**
     * Test that the backend returns a MemberList with at least one member
     * when we are authenticated as super-admin
     */
    @Test
    void givenAuthenticatedAsSuperAdmin_whenGetMembers_thenShouldReturnMembers() {
        // given
        givenAtLeastOneMemberExists();
        initRestTemplate("super-admin", "super-admin");

        // when
        ResponseEntity<MemberList> response = restTemplate.getForEntity(RESOURCE_URL, MemberList.class);

        // then
        assertValidResponse(response);
        assertValidMemberList(response.getBody());
    }

    // GET /members/{id}

    /**
     * Test that the backend returns a 401 UNAUTHORIZED HttpStatus
     * when we are not authenticated
     */
    @Test
    void givenNotAuthenticated_whenGetMember_thenShouldReturnUnauthorized() {
        // given

        // when
        ResponseEntity<MemberList> response = restTemplate.getForEntity(RESOURCE_URL, MemberList.class);

        // then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Test that the backend returns member details when we are authenticated as user
     */
    @Test
    void givenAuthenticatedAsUser_whenGetMember_thenShouldReturnMemberDetails() {
        // given
        givenAtLeastOneMemberExists();
        long id = anExistingId();
        initRestTemplate("user", "password");

        // when
        ResponseEntity<Member> response = restTemplate.getForEntity(RESOURCE_URL + "/" + id, Member.class);

        // then
        assertValidResponse(response);
        assertValidMember(response.getBody());
    }

    /**
     * Test that the backend returns member details when we are authenticated as admin
     */
    @Test
    void givenAuthenticatedAsAdmin_whenGetMember_thenShouldReturnMemberDetails() {
        // given
        givenAtLeastOneMemberExists();
        long id = anExistingId();
        initRestTemplate("admin", "admin");

        // when
        ResponseEntity<Member> response = restTemplate.getForEntity(RESOURCE_URL + "/" + id, Member.class);

        // then
        assertValidResponse(response);
        assertValidMember(response.getBody());
    }

    /**
     * Test that the backend returns member details when we are authenticated as super-admin
     */
    @Test
    void givenAuthenticatedAsSuperAdmin_whenGetMember_thenShouldReturnMemberDetails() {
        // given
        givenAtLeastOneMemberExists();
        long id = anExistingId();
        initRestTemplate("super-admin", "super-admin");

        // when
        ResponseEntity<Member> response = restTemplate.getForEntity(RESOURCE_URL + "/" + id, Member.class);

        // then
        assertValidResponse(response);
        assertValidMember(response.getBody());
    }

    /**
     * Test that the backend returns 404 NOT FOUND HttpStatus
     * when we request user details for non-existing id
     */
    @Test
    void givenAuthenticatedAsUser_whenGetNonExistingMember_thenShouldReturn404() {
        // given
        long id = Long.MAX_VALUE;
        initRestTemplate("user", "password");

        // when
        ResponseEntity<Member> response = restTemplate.getForEntity(RESOURCE_URL + "/" + id, Member.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private void assertValidResponse(ResponseEntity<?> response) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    private void assertValidMemberList(MemberList memberList) {
        assertNotNull(memberList);
        assertNotNull(memberList.getMembers());
        assertNotEquals(0, memberList.getMembers().size());
    }

    private void assertValidMember(Member member) {
        assertNotNull(member);
        assertNotNull(member.getId());
        assertNotNull(member.getFirstName());
        assertNotNull(member.getLastName());
        assertNotNull(member.getEmail());
        assertNotNull(member.getPhoneNumber());
        assertNotNull(member.getPhoneNumber());
        assertNotNull(member.getRole());
        assertNotNull(member.getKnownStitches());
        assertTrue(member.getKnownStitches().size() > 0);

        assertValidAddress(member.getAddress());
    }

    private void assertValidAddress(Address address) {
        assertNotNull(address);
        assertNotNull(address.getStreet());
        assertTrue(address.getNumber() > 0);
        assertTrue(address.getZipCode() > 0 && address.getZipCode() <= 9999);
        assertNotNull(address.getCity());
    }
}
