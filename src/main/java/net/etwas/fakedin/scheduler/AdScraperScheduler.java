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
     * Once a week at 10:00
     */
    @Scheduled(cron = "0 0 10 * * SUN")
    public void weeklyScrape() {
        log.info("Weekly bot scraping started...");
        try {
            scraperService.scrapeRecentJobs();
            log.info("Scraping successful.");
        } catch (Exception e) {
            log.error("Error while scraping", e);
        }
    }

    /**
     * For testing purposes, every 5 min
     */
    // @Scheduled(fixedRate = 300000) // 5 minuta
    // public void testScrape() {
    //     log.info("Test scraping...");
    //     scraperService.scrapeRecentJobs();
    // }
}