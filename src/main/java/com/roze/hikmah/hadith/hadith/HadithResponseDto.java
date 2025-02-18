package com.roze.hikmah.hadith.hadith;

import com.roze.hikmah.hadith.hadithgrade.HadithGrade;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HadithResponseDto {
    private Long id;
    private String text;
    private String source;
    private String book;
    private String narrator;
    private HadithGrade grade;
    private String createdBy;
    private LocalDateTime createdAt;
}
