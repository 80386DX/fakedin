package net.etwas.fakedin.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ad_appearances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdAppearance {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private Ad ad;

    private LocalDate datePosted;
    private Integer numberOfReposting;
    private LocalDateTime detectedAt;
}