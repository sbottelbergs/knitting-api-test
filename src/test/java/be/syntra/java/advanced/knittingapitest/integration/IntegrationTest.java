package be.syntra.java.advanced.knittingapitest.integration;

import be.syntra.java.advanced.knittingapitest.dto.Member;
import be.syntra.java.advanced.knittingapitest.dto.MemberList;
import be.syntra.java.advanced.knittingapitest.dto.MemberListItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;
import java.util.List;

import static be.syntra.java.advanced.knittingapitest.util.TestHelper.givenAtLeastOneMemberExists;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    private static final String RESOURCE_URL = "http://localhost:8080/members";

    @Autowired
    private TestRestTemplate restTemplate;
    private Member memberDetail;
    private MemberListItem member;

    @PostConstruct
    void init() {
        restTemplate = restTemplate.withBasicAuth("admin", "admin");
    }

    @Test
    void testIntegration() {
        givenAtLeastOneMemberExists();

        testGetMember();
        testUpdateMemberDetails();
        testDeleteMember();
    }

    // Test methods

    private void testDeleteMember() {
        // given -- authenticate as admin
        restTemplate = restTemplate.withBasicAuth("super-admin", "super-admin");

        // when -- delete member
        restTemplate.delete(RESOURCE_URL + "/" + member.getId());
        List<MemberListItem> membersAfterDelete = getMembers();

        // then -- assert member deleted
        assertFalse(membersAfterDelete.contains(member));
    }

    private void testGetMember() {
        // given -- at least one member
        List<MemberListItem> memberListItems = getMembers();
        member = memberListItems.get(0);

        // when -- get full member detail
        memberDetail = restTemplate.getForObject(RESOURCE_URL + "/" + member.getId(), Member.class);

        // then -- assert member detail equals member list item
        assertEquals(member.getId(), memberDetail.getId());
        assertTrue(member.getName().contains(memberDetail.getFirstName()));
        assertTrue(member.getName().contains(memberDetail.getLastName()));
        assertEquals(member.getKnownStitches(), memberDetail.getKnownStitches().size());
        assertEquals(member.getEmail(), memberDetail.getEmail());
    }

    private void testUpdateMemberDetails() {
        // given -- authenticated as super admin
        restTemplate = restTemplate.withBasicAuth("admin", "admin");

        // when -- update member details
        memberDetail.setFirstName("New first name");
        memberDetail.setLastName("New last name");
        restTemplate.put(RESOURCE_URL + "/" + member.getId(), memberDetail);
        final Member updatedMember = getMember(member.getId());

        // then -- assert member details updated
        assertEquals("New first name", updatedMember.getFirstName());
        assertEquals("New last name", updatedMember.getLastName());

    }


    // Helper methods

    private List<MemberListItem> getMembers() {
        return restTemplate.getForObject(RESOURCE_URL, MemberList.class).getMembers();
    }

    private Member getMember(long id) {
        return restTemplate.getForObject(RESOURCE_URL + "/" + id, Member.class);
    }

}
