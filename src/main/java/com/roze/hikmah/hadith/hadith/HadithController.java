package com.roze.hikmah.hadith.hadith;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("hadiths")
@RequiredArgsConstructor
public class HadithController {
    
    private final HadithService hadithService;

    // ✅ Create a new Hadith
    @PostMapping("/{userId}")
    public ResponseEntity<HadithResponseDto> createHadith(
            @PathVariable Long userId,
            @RequestBody HadithRequestDto requestDto) {
        return ResponseEntity.ok(hadithService.createHadith(requestDto, userId));
    }

    // ✅ Get all Hadiths
    @GetMapping
    public ResponseEntity<List<HadithResponseDto>> getAllHadiths() {
        return ResponseEntity.ok(hadithService.getAllHadiths());
    }

    // ✅ Get Hadith by ID
    @GetMapping("/{id}")
    public ResponseEntity<HadithResponseDto> getHadithById(@PathVariable Long id) {
        return ResponseEntity.ok(hadithService.getHadithById(id));
    }

    // ✅ Get Hadiths by Source
    @GetMapping("/source/{source}")
    public ResponseEntity<List<HadithResponseDto>> getHadithsBySource(@PathVariable String source) {
        return ResponseEntity.ok(hadithService.getHadithsBySource(source));
    }

    // ✅ Get Hadiths by Book
    @GetMapping("/book/{book}")
    public ResponseEntity<List<HadithResponseDto>> getHadithsByBook(@PathVariable String book) {
        return ResponseEntity.ok(hadithService.getHadithsByBook(book));
    }

    // ✅ Get Hadiths by Narrator
    @GetMapping("/narrator/{narrator}")
    public ResponseEntity<List<HadithResponseDto>> getHadithsByNarrator(@PathVariable String narrator) {
        return ResponseEntity.ok(hadithService.getHadithsByNarrator(narrator));
    }

    // ✅ Update a Hadith
    @PutMapping("/{id}")
    public ResponseEntity<HadithResponseDto> updateHadith(
            @PathVariable Long id,
            @RequestBody HadithRequestDto requestDto) {
        return ResponseEntity.ok(hadithService.updateHadith(id, requestDto));
    }

    // ✅ Delete a Hadith
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHadith(@PathVariable Long id) {
        hadithService.deleteHadith(id);
        return ResponseEntity.noContent().build();
    }
}
