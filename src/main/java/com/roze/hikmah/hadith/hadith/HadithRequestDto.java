package com.roze.hikmah.hadith.hadith;

import com.roze.hikmah.hadith.hadithgrade.HadithGrade;
import lombok.Data;

@Data
public class HadithRequestDto {
    private String text;
    private String source;
    private String book;
    private String narrator;
    private HadithGrade grade;
}
