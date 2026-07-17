package net.etwas.fakedin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseStatus {

    SUSPICIOUS("Highly suspicious - multiple reposts"),
    ERROR("Something went wrong"),
    REPOSTED("Reposted ad"),
    NEW("First time ad");

    private final String status;

}
 