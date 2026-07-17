package net.etwas.fakedin.mapper;

import lombok.extern.slf4j.Slf4j;
import net.etwas.fakedin.dto.AdRequestDTO;
import net.etwas.fakedin.dto.BotScraping;
import net.etwas.fakedin.model.OfficeType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
@Slf4j
public class ConvertorClass {

    public AdRequestDTO convertToAdRequest(BotScraping scraping) {
        OfficeType officeType = parseOfficeType(scraping.officeType());
        LocalDate datePosted = parseDate(scraping.datePosted());

        return new AdRequestDTO(
                scraping.companyName(),
                scraping.jobTitle(),
                scraping.cityAndState(),
                officeType,
                scraping.description(),
                datePosted
        );
    }

    private OfficeType parseOfficeType(String value) {
        if (value == null || value.isBlank()) {
            return OfficeType.HYBRID; // default
        }
        try {
            return OfficeType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            String upper = value.toUpperCase();
            if (upper.contains("REMOTE")) return OfficeType.REMOTE;
            if (upper.contains("ONSITE") || upper.contains("ON-SITE")) return OfficeType.ONSITE;
            return OfficeType.HYBRID;
        }
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(dateStr); // yyyy-MM-dd format
        } catch (Exception e) {
            log.warn("Failure while parsing date: {}", dateStr);
            return LocalDate.now();
        }
    }


}
