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
        // Given
        Ad existingAd = Ad.builder().id(1L).build();

        when(adRepository.findByContentHash(anyString()))
                .thenReturn(Optional.of(existingAd));

        when(appearanceRepository.countByAdId(1L))
                .thenReturn(5L);   // 5 appearances

        // When
        AdCheckResponse response = adService.processAd(testDto);

        // Then
        assertEquals(ResponseStatus.SUSPICIOUS, response.status());
        assertEquals(6, response.repostedCount());
    }

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
        // Given
        AdRequestDTO badDto = new AdRequestDTO(
                "Test Company",
                "Test Job",
                null,
                null,
                null,
                null
        );

        when(adRepository.findByContentHash(anyString())).thenReturn(Optional.empty());
        when(adRepository.save(any(Ad.class))).thenAnswer(inv -> {
            Ad ad = inv.getArgument(0);
            ad.setId(99L);
            return ad;
        });

        // When & Then
        assertDoesNotThrow(() -> {
            AdCheckResponse response = adService.processAd(badDto);
            assertEquals(ResponseStatus.NEW, response.status());
            assertEquals(1, response.repostedCount());
        });
    }

}