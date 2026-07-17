package net.etwas.fakedin.dto;

public record BotScraping(String companyName,
                          String jobTitle,
                          String description,
                          String cityAndState,
                          String officeType,
                          String datePosted ) {}
