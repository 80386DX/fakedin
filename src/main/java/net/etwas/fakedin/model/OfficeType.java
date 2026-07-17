package net.etwas.fakedin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OfficeType {

    REMOTE("Remote"),
    ONSITE("On-site"),
    HYBRID("Hybrid");

    private final String variation;

}
