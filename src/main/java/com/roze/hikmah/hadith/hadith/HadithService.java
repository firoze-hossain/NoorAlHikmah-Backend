package com.roze.hikmah.hadith.hadith;

import com.roze.hikmah.auth.User;
import com.roze.hikmah.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HadithService {
    private final HadithRepository hadithRepository;
    private final UserRepository userRepository;
    private final HadithMapper hadithMapper;

    // Create Hadith
    public HadithResponseDto createHadith(HadithRequestDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Hadith hadith = hadithMapper.toEntity(dto);
        hadith.setCreatedBy(user);
        Hadith savedHadith = hadithRepository.save(hadith);
        return hadithMapper.toDto(savedHadith);
    }

    // Get All Hadiths
    public List<HadithResponseDto> getAllHadiths() {
        return hadithRepository.findAll()
                .stream()
                .map(hadithMapper::toDto)
                .collect(Collectors.toList());
    }

    // Get Hadith by ID
    public HadithResponseDto getHadithById(Long id) {
        Hadith hadith = hadithRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hadith not found"));
        return hadithMapper.toDto(hadith);
    }

    // Update Hadith
    public HadithResponseDto updateHadith(Long id, HadithRequestDto dto) {
        Hadith hadith = hadithRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hadith not found"));

        hadith.setText(dto.getText());
        hadith.setSource(dto.getSource());
        hadith.setBook(dto.getBook());
        hadith.setNarrator(dto.getNarrator());
        hadith.setGrade(dto.getGrade());

        Hadith updatedHadith = hadithRepository.save(hadith);
        return hadithMapper.toDto(updatedHadith);
    }

    // Delete Hadith
    public void deleteHadith(Long id) {
        Hadith hadith = hadithRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hadith not found"));
        hadithRepository.delete(hadith);
    }

    public List<HadithResponseDto> getHadithsBySource(String source) {
        return hadithRepository.findBySource(source)
                .stream()
                .map(hadithMapper::toDto)
                .toList();
    }

    public List<HadithResponseDto> getHadithsByBook(String book) {
        return hadithRepository.findByBook(book)
                .stream()
                .map(hadithMapper::toDto)
                .toList();
    }

    public List<HadithResponseDto> getHadithsByNarrator(String narrator) {
        return hadithRepository.findByNarrator(narrator)
                .stream()
                .map(hadithMapper::toDto)
                .toList();
    }
}
