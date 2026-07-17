package net.etwas.fakedin.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.etwas.fakedin.scheduler.LinkedInScraperService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdScraperScheduler {

    private final LinkedInScraperService scraperService;

    /**
     * Jednom nedeljno - nedeljom u 10:00 ujutru
     */
    @Scheduled(cron = "0 0 10 * * SUN")
    public void weeklyScrape() {
        log.info("Pokrećem nedeljni scraping oglasa...");
        try {
            scraperService.scrapeRecentJobs();
            log.info("Nedeljni scraping završen uspešno.");
        } catch (Exception e) {
            log.error("Greška prilikom nedeljnog scrapinga", e);
        }
    }

    /**
     * Za testiranje - svakih 5 minuta (zakomentariši kasnije)
     */
    // @Scheduled(fixedRate = 300000) // 5 minuta
    // public void testScrape() {
    //     log.info("Test scraping...");
    //     scraperService.scrapeRecentJobs();
    // }
}