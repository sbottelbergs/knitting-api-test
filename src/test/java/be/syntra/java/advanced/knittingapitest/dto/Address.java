package be.syntra.java.advanced.knittingapitest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Address {
    private String street;
    private int number;
    private String poBox;
    private int zipCode;
    private String city;
}
