package be.syntra.java.advanced.knittingapitest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class Member {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate birthDate;
    private Role role;
    private Set<KnittingStitch> knownStitches;
    private Address address;
}
