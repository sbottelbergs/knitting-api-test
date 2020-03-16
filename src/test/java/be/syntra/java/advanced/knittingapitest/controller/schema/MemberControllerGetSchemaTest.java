package be.syntra.java.advanced.knittingapitest.controller.schema;

import be.syntra.java.advanced.knittingapitest.dto.Address;
import be.syntra.java.advanced.knittingapitest.dto.KnittingStitch;
import be.syntra.java.advanced.knittingapitest.dto.Role;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static be.syntra.java.advanced.knittingapitest.util.TestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class MemberControllerGetSchemaTest {

    @Test
    void testMemberListHasCorrectSchema() {
        // given -- get members
        givenAtLeastOneMemberExists();
        Response response = RestAssured.given()
                .auth().basic("user", "password")
                .get(RESOURCE_URL);

        // when
        JsonPath jsonPath = response.jsonPath();

        // then -- assert correct shape
        assertNotNull(jsonPath.getList("members"));
        assertTrue(jsonPath.getList("members").size() > 0);

        assertNotNull(jsonPath.getString("members[0].id"));
        assertNotNull(jsonPath.getString("members[0].name"));
        assertNotNull(jsonPath.getString("members[0].email"));
        assertTrue(jsonPath.getInt("members[0].knownStitches") > 0);
        assertNotNull(jsonPath.getString("members[0].role"));

        assertNull(jsonPath.getString("members[0].firstName"));
        assertNull(jsonPath.getString("members[0].lastName"));
        assertNull(jsonPath.getString("members[0].phoneNumber"));
        assertNull(jsonPath.getString("members[0].birthDate"));
        assertNull(jsonPath.getString("members[0].address"));
    }


    @Test
    void testMemberDetailHasCorrectSchema() {
        // given -- get members
        givenAtLeastOneMemberExists();
        Response response = RestAssured.given()
                .auth().basic("user", "password")
                .get(RESOURCE_URL + "/" + anExistingId()).andReturn();

        // when
        JsonPath jsonPath = new JsonPath(response.getBody().asString());

        // then -- assert correct shape
        assertNotNull(jsonPath.getString("id"));
        assertNotNull(jsonPath.getString("firstName"));
        assertNotNull(jsonPath.getString("lastName"));
        assertNotNull(jsonPath.getString("email"));
        assertNotNull(jsonPath.getString("phoneNumber"));
        assertNotNull(jsonPath.getString("birthDate"));
        assertNotNull(jsonPath.getString("role"));
        assertNotNull(jsonPath.getString("knownStitches"));
        assertTrue(jsonPath.getList("knownStitches").size() > 0);
        assertNotNull(jsonPath.getString("address"));
    }

    @Test
    void testAddressHasCorrectSchema() {
        // given -- an existing member
        givenAtLeastOneMemberExists();
        Response response = RestAssured.given()
                .auth().basic("user", "password")
                .get(RESOURCE_URL + "/" + anExistingId()).andReturn();

        // when
        JsonPath jsonPath = new JsonPath(response.getBody().asString());

        // then -- assert address has correct schema
        assertNull(jsonPath.getString("address.id"));

        assertNotNull(jsonPath.getString("address.street"));
        assertNotNull(jsonPath.getString("address.number"));
        assertNotNull(jsonPath.getString("address.zipCode"));
        assertNotNull(jsonPath.getString("address.city"));
    }

}

