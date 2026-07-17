package net.etwas.fakedin;

import net.etwas.fakedin.dto.AdRequestDTO;
import net.etwas.fakedin.dto.AdCheckResponse;
import net.etwas.fakedin.dto.ResponseStatus;
import net.etwas.fakedin.model.Ad;
import net.etwas.fakedin.model.AdAppearance;
import net.etwas.fakedin.model.OfficeType;
import net.etwas.fakedin.repository.AdRepo;
import net.etwas.fakedin.repository.AdAppearanceRepo;
import net.etwas.fakedin.service.AdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdServiceTest {

    @Mock
    private AdRepo adRepository;

    @Mock
    private AdAppearanceRepo appearanceRepository;

    @InjectMocks
    private AdService adService;

    private AdRequestDTO testDto;

    @BeforeEach
    void setUp() {
        testDto = new AdRequestDTO(
                "Fubu",
                "Backend Engineer",
                "España",
                OfficeType.REMOTE,
                "Test description for the job...",
                LocalDate.now()
        );
    }

    @Test
    void shouldCreateNewAdWhenNotExists() {
        // Given
        when(adRepository.findByContentHash(anyString())).thenReturn(Optional.empty());
        when(adRepository.save(any(Ad.class))).thenAnswer(inv -> {
            Ad ad = inv.getArgument(0);
            ad.setId(1L); // simulate ID
            return ad;
        });

        // When
        AdCheckResponse response = adService.processAd(testDto);

        // Then
        assertEquals(ResponseStatus.NEW, response.status());
        assertEquals(1, response.repostedCount());
        verify(adRepository, times(1)).save(any(Ad.class));
        verify(appearanceRepository, times(1)).save(any(AdAppearance.class));
    }

    @Test
    void shouldIncrementRepostWhenAdExists() {
        // Given
        Ad existingAd = Ad.builder()
                .id(1L)
                .companyName("Fubu")
                .jobTitle("Backend Engineer")
                .contentHash("somehash")
                .build();

        when(adRepository.findByContentHash(anyString())).thenReturn(Optional.of(existingAd));
        when(appearanceRepository.countByAdId(1L)).thenReturn(2L);

        // When
        AdCheckResponse response = adService.processAd(testDto);

        // Then
        assertEquals(ResponseStatus.REPOSTED, response.status());
        assertEquals(3, response.repostedCount());
        verify(adRepository, never()).save(any(Ad.class)); // does not record new Ad
        verify(appearanceRepository, times(1)).save(any(AdAppearance.class));
    }

    @Test
    void shouldReturnSuspiciousWhenManyReposts() {
        Ad existingAd = Ad.builder().id(1L).build();

        when(adRepository.findByContentHash(anyString())).thenReturn(Optional.of(existingAd));
        when(appearanceRepository.countByAdId(1L)).thenReturn(5L); // more than 3

        AdCheckResponse response = adService.processAd(testDto);

        assertEquals(ResponseStatus.SUSPICIOUS, response.status());
        assertEquals(6, response.repostedCount());
    }

//    @Test
//    void shouldGenerateConsistentHashForSameInput() {
//        // Given
//        when(adRepository.findByContentHash(anyString())).thenReturn(Optional.empty());
//        when(adRepository.save(any(Ad.class))).thenAnswer(inv -> {
//            Ad ad = inv.getArgument(0);
//            ad.setId(1L);
//            return ad;
//        });
//
//        // When
//        AdCheckResponse response1 = adService.processAd(testDto);
//        AdCheckResponse response2 = adService.processAd(testDto);
//
//        // Then
//        assertEquals(ResponseStatus.NEW, response1.status());
//        assertEquals(ResponseStatus.REPOSTED, response2.status()); // Second time should be repost
//    }

//    @Test
//    void shouldDetectRepostOnSecondCallWithSameData() {
//        // Given
//        Ad existingAd = Ad.builder()
//                .id(1L)
//                .companyName(testDto.companyName())
//                .jobTitle(testDto.jobTitle())
//                .contentHash("some-fixed-hash") // simuliramo da hash postoji
//                .build();
//
//        when(adRepository.findByContentHash(anyString())).thenReturn(Optional.of(existingAd));
//        when(appearanceRepository.countByAdId(1L)).thenReturn(0L); // prvi appearance
//
//        // When
//        AdCheckResponse firstResponse = adService.processAd(testDto);   // prvi put
//        AdCheckResponse secondResponse = adService.processAd(testDto);  // drugi put isti podaci
//
//        // Then
//        assertEquals(ResponseStatus.NEW, firstResponse.status());
//        assertEquals(1, firstResponse.repostedCount());
//
//        assertEquals(ResponseStatus.REPOSTED, secondResponse.status());
//        assertEquals(2, secondResponse.repostedCount());
//    }

//    @Test
//    void shouldDetectRepostOnSecondCallWithSameData() {
//        // Given
//        Ad existingAd = Ad.builder()
//                .id(1L)
//                .companyName(testDto.companyName())
//                .jobTitle(testDto.jobTitle())
//                .contentHash("fixed-hash-for-test")
//                .build();
//
//        // Važno: isti hash za oba poziva
//        when(adRepository.findByContentHash(anyString()))
//                .thenReturn(Optional.of(existingAd));
//
//        when(appearanceRepository.countByAdId(1L))
//                .thenReturn(0L);   // pre prvog appearance-a
//
//        // When
//        AdCheckResponse first = adService.processAd(testDto);
//        AdCheckResponse second = adService.processAd(testDto);
//
//        // Then
//        assertEquals(ResponseStatus.NEW, first.status());
//        assertEquals(1, first.repostedCount());
//
//        assertEquals(ResponseStatus.REPOSTED, second.status());
//        assertEquals(2, second.repostedCount());
//    }

    @Test
    void shouldDetectRepostOnSecondCallWithSameData() {
        // Given
        Ad existingAd = Ad.builder()
                .id(1L)
                .build();

        when(adRepository.findByContentHash(anyString()))
                .thenReturn(Optional.of(existingAd));

        when(appearanceRepository.countByAdId(1L))
                .thenReturn(0L)   // first call
                .thenReturn(1L);  // second call

        // When
        AdCheckResponse first = adService.processAd(testDto);
        AdCheckResponse second = adService.processAd(testDto);

        // Then
        assertEquals(ResponseStatus.NEW, first.status());
        assertEquals(1, first.repostedCount());

        assertEquals(ResponseStatus.REPOSTED, second.status());
        assertEquals(2, second.repostedCount());
    }

    @Test
    void shouldHandleNullValuesGracefully() {
        AdRequestDTO nullDto = new AdRequestDTO("Company", "Title", null, null, null, null);

        when(adRepository.findByContentHash(anyString())).thenReturn(Optional.empty());
        when(adRepository.save(any())).thenReturn(new Ad());

        assertDoesNotThrow(() -> adService.processAd(nullDto));
    }

}