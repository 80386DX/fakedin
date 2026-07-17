package net.etwas.fakedin.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.etwas.fakedin.dto.AdRequestDTO;
import net.etwas.fakedin.service.AdService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkedInScraperService {

    private final AdService adService;
    // private final PythonBotClient pythonClient;

    public void scrapeRecentJobs() {
        log.info("Scraping begun...");

        try {
            // TODO:  Python scraper call
            // List<AdRequestDTO> newAds = callPythonScraper();

            // Simulate
            // for (AdRequestDTO ad : newAds) {
            //     adService.processAd(ad);
            // }

            log.info("Scraping done.");

        } catch (Exception e) {
            log.error("Error while scraping ad", e);
        }
    }


    // private List<AdRequestDTO> callPythonScraper() {
    //     // TODO: call scraper
    //     return List.of();
    // }
}