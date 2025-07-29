// src/main/java/portal/forasbackend/controller/Candidate/CandidateController.java
package portal.forasbackend.web.controller.v1.candidate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.candidate.UpdateCandidateProfileRequest;
import portal.forasbackend.dto.request.candidate.UpdateCredentialsRequest;
import portal.forasbackend.dto.response.candidate.CandidateProfileDto;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.service.Candidate.CandidateService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/candidate")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping("/{id}/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDATE')")
    public ResponseEntity<CandidateProfileDto> getCandidateProfile(@PathVariable Long id) {
        CandidateProfileDto profile = candidateService.getCandidateDetails(id);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileDto> getMyProfile(@AuthenticationPrincipal Candidate candidate) {
        log.info("Getting profile for candidate: {}", candidate.getId());
        CandidateProfileDto profile = candidateService.getCandidateDetails(candidate.getId());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileDto> updateMyProfile(
            @AuthenticationPrincipal Candidate candidate,
            @Valid @RequestBody UpdateCandidateProfileRequest request) {
        log.info("Updating profile for candidate: {}", candidate.getId());
        CandidateProfileDto updatedProfile = candidateService.updateCandidateProfile(candidate.getId(), request);
        return ResponseEntity.ok(updatedProfile);
    }

    @PutMapping("/me/update-credentials")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> updateCredentials(
            @AuthenticationPrincipal Candidate candidate,
            @Valid @RequestBody UpdateCredentialsRequest request) {
        log.info("Updating credentials for candidate: {}", candidate.getId());

        try {
            candidateService.updateCredentials(candidate.getId(), request);
            return ResponseEntity.ok(Map.of("message", "تم تحديث البيانات بنجاح"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/me/avatar")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Map<String, String>> uploadAvatar(
            @AuthenticationPrincipal Candidate candidate,
            @RequestParam("avatar") MultipartFile file) {
        log.info("Uploading avatar for candidate: {}", candidate.getId());

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5MB limit
            return ResponseEntity.badRequest().body(Map.of("error", "File size exceeds 5MB limit"));
        }

        String avatarUrl = candidateService.updateCandidateAvatar(candidate.getId(), file);
        return ResponseEntity.ok(Map.of("avatarUrl", avatarUrl));
    }
}