package net.etwas.fakedin.dto;

import net.etwas.fakedin.model.OfficeType;
import java.time.LocalDate;

public record AdRequestDTO(String companyName,
                           String jobTitle,
                           String cityAndState,
                           OfficeType officeType,
                           String description,
                           LocalDate datePosted) {}
