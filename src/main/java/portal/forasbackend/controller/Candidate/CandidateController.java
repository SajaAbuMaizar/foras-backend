package portal.forasbackend.controller.Candidate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.candidate.UpdateCandidateProfileRequest;
import portal.forasbackend.dto.response.FileUploadResponse;
import portal.forasbackend.dto.response.candidate.CandidateProfileDto;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.exception.FileUploadException;
import portal.forasbackend.service.Candidate.CandidateService;

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

    @PostMapping("/me/avatar")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<FileUploadResponse> uploadAvatar(
            @AuthenticationPrincipal Candidate candidate,
            @RequestParam("avatar") MultipartFile file) {
        log.info("Uploading avatar for candidate: {}", candidate.getId());

        try {
            String avatarUrl = candidateService.updateCandidateAvatar(candidate.getId(), file);
            return ResponseEntity.ok(FileUploadResponse.builder()
                    .success(true)
                    .url(avatarUrl)
                    .message("تم رفع الصورة الشخصية بنجاح")
                    .build());
        } catch (FileUploadException e) {
            log.error("File upload error for candidate {}: {}", candidate.getId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(FileUploadResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .errorCode(e.getErrorCode())
                            .build());
        } catch (Exception e) {
            log.error("Unexpected error uploading avatar for candidate {}", candidate.getId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(FileUploadResponse.builder()
                            .success(false)
                            .message("حدث خطأ غير متوقع. يرجى المحاولة مرة أخرى")
                            .errorCode("UNEXPECTED_ERROR")
                            .build());
        }
    }
}