package com.roze.hikmah.hadith.hadith;

import org.springframework.stereotype.Component;

@Component
public class HadithMapper {

    // Convert Request DTO to Entity
    public Hadith toEntity(HadithRequestDto dto) {
        return Hadith.builder()
                .text(dto.getText())
                .source(dto.getSource())
                .book(dto.getBook())
                .narrator(dto.getNarrator())
                .grade(dto.getGrade())
                .build();
    }

    // Convert Entity to Response DTO
    public HadithResponseDto toDto(Hadith hadith) {
        HadithResponseDto dto = new HadithResponseDto();
        dto.setId(hadith.getId());
        dto.setText(hadith.getText());
        dto.setSource(hadith.getSource());
        dto.setBook(hadith.getBook());
        dto.setNarrator(hadith.getNarrator());
        dto.setGrade(hadith.getGrade());
        dto.setCreatedBy(hadith.getCreatedBy().getUsername()); // Assuming User has a getUsername() method
        dto.setCreatedAt(hadith.getCreatedAt());
        return dto;
    }
}
