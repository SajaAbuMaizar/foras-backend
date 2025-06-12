package portal.forasbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.dto.response.candidate.CandidateProfileDto;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.service.CandidateService;

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
        CandidateProfileDto profile = candidateService.getCandidateDetails(candidate.getId());
        return ResponseEntity.ok(profile);
    }
}