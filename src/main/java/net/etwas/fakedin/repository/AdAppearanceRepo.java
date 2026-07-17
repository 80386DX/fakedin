package net.etwas.fakedin.repository;

import net.etwas.fakedin.model.AdAppearance;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdAppearanceRepo {

    List<AdAppearance> findByAdId(Long adId);

    long countByAdId(Long adId);

    List<AdAppearance> findByAdIdAndDatePostedAfter(Long adId, LocalDate date);

    void save(AdAppearance appearance);
}
