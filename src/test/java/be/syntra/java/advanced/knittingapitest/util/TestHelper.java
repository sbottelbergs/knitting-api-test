package be.syntra.java.advanced.knittingapitest.util;

import be.syntra.java.advanced.knittingapitest.dto.Address;
import be.syntra.java.advanced.knittingapitest.dto.KnittingStitch;
import be.syntra.java.advanced.knittingapitest.dto.Member;
import be.syntra.java.advanced.knittingapitest.dto.Role;
import io.restassured.RestAssured;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Month;
import java.util.Random;
import java.util.Set;

public class TestHelper {
    public static final String RESOURCE_URL = "http://localhost:8080/members";

    /**
     * Verifies that at least one member exists on the server.
     * Creates a new member if none exist.
     *
     * @throws RuntimeException when unable to create a new member
     */
    public static void givenAtLeastOneMemberExists() {
        if (getNumberOfExistingMembers() == 0) {
            TestRestTemplate testRestTemplate = new TestRestTemplate().withBasicAuth("admin", "admin");
            ResponseEntity<?> responseEntity = testRestTemplate.postForEntity(RESOURCE_URL, aMember(), null);

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Unable to create a member");
            }
        }
    }

    /**
     * Get the details for a new member.
     *
     * @return the details for a member
     */
    public static Member aMember() {
        return Member.builder()
                .firstName("First name")
                .lastName("Last name")
                .email("first.last@email.com")
                .birthDate(LocalDate.of(1989, Month.JUNE, 13))
                .phoneNumber("011/12.34.56")
                .knownStitches(Set.of(KnittingStitch.CABLE, KnittingStitch.BEGINNER_LACE))
                .role(Role.MEMBER)
                .address(
                        Address.builder()
                                .street("A Street")
                                .number(123)
                                .zipCode(1234)
                                .city("City")
                                .build()
                )
                .build();
    }

    /**
     * Get incomplete details for a new member.
     *
     * @return incomplete member
     */
    public static Member anInvalidMember() {
        return Member.builder()
                .firstName(null)
                .lastName(null)
                .email(null)
                .birthDate(LocalDate.of(3000, Month.MARCH, 3))
                .phoneNumber(null)
                .knownStitches(Set.of())
                .role(null)
                .address(
                        Address.builder()
                                .street(null)
                                .number(-123)
                                .zipCode(-1234)
                                .city(null)
                                .build()
                )
                .build();
    }

    /**
     * Get the id of a member that exists on the server.
     *
     * @return the id of an existing member
     * @throws RuntimeException if no member found on the server
     */
    public static long anExistingId() {
        int numberOfMembers = getNumberOfExistingMembers();

        if (numberOfMembers == 0) {
            throw new RuntimeException("No members exist on the server");
        }

        return RestAssured.given()
                .auth()
                .preemptive().basic("user", "password")
                .get(RESOURCE_URL).andReturn()
                .body()
                .jsonPath()
                .getInt(
                        String.format(
                                "members[%d].id",
                                new Random().nextInt(numberOfMembers)
                        )
                );
    }

    private static int getNumberOfExistingMembers() {
        return RestAssured
                .given().auth()
                .preemptive().basic("user", "password")
                .get(RESOURCE_URL).body().jsonPath().getList("members").size();
    }
}
