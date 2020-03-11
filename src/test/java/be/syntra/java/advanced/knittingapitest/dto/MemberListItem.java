package be.syntra.java.advanced.knittingapitest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemberListItem {
    private Long id;
    private String name;
    private String email;
    private int knownStitches;
}
