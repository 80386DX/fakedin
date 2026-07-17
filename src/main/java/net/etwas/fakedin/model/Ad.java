package net.etwas.fakedin.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ad {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String jobTitle;
    private String cityAndState;

    @Enumerated(EnumType.STRING)
    private OfficeType officeType;
    private String description;

    @Column(unique = true, nullable = false)
    private String contentHash;

}