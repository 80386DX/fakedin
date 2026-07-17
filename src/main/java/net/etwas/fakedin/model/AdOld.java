package net.etwas.fakedin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@Entity (name = "ads")
public class AdOld {

    //This was my starting class but decided to split after "talking" with herr Grok.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final String companyName;
    private final String jobTitle;
    private String cityAndState;

    @Enumerated(EnumType.STRING)
    private OfficeType officeType;
    private LocalDate datePosted;
    private final String description;
    private boolean reposted;
    private Integer numberOfReposting;

}
