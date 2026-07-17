package net.etwas.fakedin.repository;

import net.etwas.fakedin.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdRepo extends JpaRepository<Ad, Long> {

    Optional<Ad> findByContentHash(String contentHash);

    Optional<Ad> findByCompanyNameAndJobTitle(String companyName, String jobTitle);

}
