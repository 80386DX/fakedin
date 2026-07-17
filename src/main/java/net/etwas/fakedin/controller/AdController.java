package net.etwas.fakedin.controller;

import net.etwas.fakedin.dto.AdCheckResponse;
import net.etwas.fakedin.dto.AdRequestDTO;
import net.etwas.fakedin.dto.UrlRequest;
import net.etwas.fakedin.exception.InvalidLinkedInUrlException;
import net.etwas.fakedin.service.AdService;
import net.etwas.fakedin.dto.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    @PostMapping("/check")
    public ResponseEntity<AdCheckResponse> checkAdByUrl(@RequestBody UrlRequest urlRequest) {

        String url = urlRequest.url();

        if (url == null || url.isBlank() || !isValidLinkedInJobUrl(url)) {
            throw new InvalidLinkedInUrlException("URL must be LinkedIn link (linkedin.com/jobs/view)");
        }

        // TODO: Ovde će kasnije ići poziv Python scraper-u
        // AdRequestDTO dto = scraperService.scrapeLinkedInUrl(url);

        // Za sada samo test
        // return ResponseEntity.ok(adService.processAd(dto));

        return ResponseEntity.ok(new AdCheckResponse(ResponseStatus.ERROR, 0));
    }

    // Ako želiš i direktan unos podataka (za testiranje)
    @PostMapping("/check-direct")
    public ResponseEntity<AdCheckResponse> checkDirect(@RequestBody AdRequestDTO request) {
        AdCheckResponse response = adService.processAd(request);
        return ResponseEntity.ok(response);
    }

    //Helper method for verification
    private boolean isValidLinkedInJobUrl(String url) {
        if (url == null) return false;
        String lowerUrl = url.toLowerCase().trim();
        return lowerUrl.contains("linkedin.com/jobs/view") ||
                lowerUrl.contains("linkedin.com/jobs/search");
    }
}