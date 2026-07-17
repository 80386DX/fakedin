package net.etwas.fakedin.controller;

import net.etwas.fakedin.dto.BotScraping;
import net.etwas.fakedin.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/bot")
public class BotController {

    @Autowired
    private BotService service;

    private static final int BATCH_SIZE = 100;


    //This resource is for bot's scraping
    @PostMapping("/scraping")
    public void scrapingJobs(@RequestBody List<BotScraping> list) {

        if(list.size() > BATCH_SIZE){
            throw new IllegalArgumentException("Batch is too big");
        }

        service.saveAll(list);

    }

}
