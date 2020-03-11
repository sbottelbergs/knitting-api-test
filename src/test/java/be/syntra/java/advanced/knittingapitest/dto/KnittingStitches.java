package be.syntra.java.advanced.knittingapitest.dto;

import lombok.Data;

import java.util.Set;

@Data
public class KnittingStitches {
    private final Set<KnittingStitch> stitches;
}
