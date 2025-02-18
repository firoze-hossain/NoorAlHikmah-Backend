package com.roze.hikmah.hadith.hadithgrade;

import lombok.Getter;

@Getter
public enum HadithGrade {
    SAHIH(1L, "Sahih"),  // Authentic
    HASAN(2L, "Hasan"),  // Good
    DAIF(3L, "Daif"),   // Weak
    MAWDU(4L, "Mawdu");    // Fabricated

    private final Long value;
    private final String name;

    HadithGrade(Long value, String name) {
        this.value = value;
        this.name = name;
    }
}
