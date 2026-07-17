package net.etwas.fakedin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.etwas.fakedin.dto.AdRequestDTO;
import net.etwas.fakedin.dto.BotScraping;
import net.etwas.fakedin.mapper.ConvertorClass;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotService {

    private final AdService adService;
    private final ConvertorClass convertor;


    public void saveAll(List<BotScraping> scrapings) {
        if (scrapings == null || scrapings.isEmpty()) {
            return;
        }

        log.info("Size of  {} job being processed...", scrapings.size());

        int savedCount = 0;

        for (BotScraping scraping : scrapings) {
            try {
                AdRequestDTO dto = convertor.convertToAdRequest(scraping);
                adService.processAd(dto);
                savedCount++;
            } catch (Exception e) {
                log.error("Error while parsing job: {}", scraping, e);
            }
        }

        log.info("Bot made {} of {} ads successful.", savedCount, scrapings.size());
    }
}