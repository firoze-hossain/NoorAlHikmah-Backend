package com.roze.hikmah.hadith.hadith;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HadithRepository extends JpaRepository<Hadith, Long> {
    List<Hadith> findBySource(String source);
    List<Hadith> findByBook(String book);
    List<Hadith> findByNarrator(String narrator);
}
