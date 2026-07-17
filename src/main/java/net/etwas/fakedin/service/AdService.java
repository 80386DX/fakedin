package net.etwas.fakedin.service;

import net.etwas.fakedin.dto.AdRequestDTO;
import net.etwas.fakedin.dto.AdCheckResponse;
import net.etwas.fakedin.model.Ad;
import net.etwas.fakedin.model.AdAppearance;
import net.etwas.fakedin.dto.ResponseStatus;
import net.etwas.fakedin.repository.AdRepo;
import net.etwas.fakedin.repository.AdAppearanceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepo adRepository;
    private final AdAppearanceRepo appearanceRepository;

    @Transactional
    public AdCheckResponse processAd(AdRequestDTO request) {
        String contentHash = generateContentHash(request);

        Optional<Ad> existingAd = adRepository.findByContentHash(contentHash);

        Ad ad;
        int repostCount;

        if (existingAd.isPresent()) {
            ad = existingAd.get();
            repostCount = (int) appearanceRepository.countByAdId(ad.getId()) + 1;
        } else {
            ad = Ad.builder()
                    .companyName(request.companyName())
                    .jobTitle(request.jobTitle())
                    .cityAndState(request.cityAndState())
                    .officeType(request.officeType())
                    .description(request.description())
                    .contentHash(contentHash)
                    .build();

            ad = adRepository.save(ad);
            repostCount = 1;
        }

        AdAppearance appearance = AdAppearance.builder()
                .ad(ad)
                .datePosted(request.datePosted() != null ? request.datePosted() : LocalDate.now())
                .detectedAt(LocalDateTime.now())
                .build();

        appearanceRepository.save(appearance);

        ResponseStatus status = determineStatus(repostCount);

        return new AdCheckResponse(status, repostCount);
    }

    private String generateContentHash(AdRequestDTO dto) {
        String normalized = String.join("|",
                safeTrim(dto.companyName()).toLowerCase(),
                safeTrim(dto.jobTitle()).toLowerCase(),
                safeTrim(dto.cityAndState()).toLowerCase(),
                normalizeDescription(dto.description())
        );

        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(normalized);
    }

    private String normalizeDescription(String desc) {
        if (desc == null || desc.isBlank()) return "";
        return desc.toLowerCase()
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private ResponseStatus determineStatus(int repostCount) {
        if (repostCount == 1) return ResponseStatus.NEW;
        if (repostCount <= 3) return ResponseStatus.REPOSTED;
        return ResponseStatus.SUSPICIOUS;
    }
}