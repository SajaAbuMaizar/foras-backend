package portal.forasbackend.controller.Employer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.employer.UpdateEmployerProfileRequest;
import portal.forasbackend.dto.response.employer.EmployerProfileResponse;
import portal.forasbackend.domain.model.Employer;
import portal.forasbackend.service.Employer.EmployerProfileService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/employer/profile")
@RequiredArgsConstructor
@PreAuthorize("hasRole('EMPLOYER')")
public class EmployerProfileController {

    private final EmployerProfileService employerProfileService;

    @GetMapping
    public ResponseEntity<EmployerProfileResponse> getProfile(
            @AuthenticationPrincipal Employer employer) {

        EmployerProfileResponse profile = employerProfileService.getEmployerProfile(employer.getId());
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<EmployerProfileResponse> updateProfile(
            @AuthenticationPrincipal Employer employer,
            @Valid @RequestBody UpdateEmployerProfileRequest request) {
        System.out.println("Updating profile for employer: " + employer.getId());

        try {
            EmployerProfileResponse updatedProfile =
                    employerProfileService.updateEmployerProfile(employer.getId(), request);
            return ResponseEntity.ok(updatedProfile);

        } catch (IllegalArgumentException e) {
            log.warn("Profile update failed for employer {}: {}", employer.getId(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(null);
        } catch (Exception e) {
            log.error("Error updating profile for employer {}: {}", employer.getId(), e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/logo")
    public ResponseEntity<?> uploadLogo(
            @AuthenticationPrincipal Employer employer,
            @RequestParam("logo") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "File is empty"));
        }

        try {
            String logoUrl = employerProfileService.updateEmployerLogo(employer.getId(), file);
            return ResponseEntity.ok(Map.of(
                    "message", "Logo uploaded successfully",
                    "logoUrl", logoUrl
            ));

        } catch (Exception e) {
            log.error("Error uploading logo for employer {}: {}", employer.getId(), e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Failed to upload logo"));
        }
    }

    @DeleteMapping("/logo")
    public ResponseEntity<?> deleteLogo(
            @AuthenticationPrincipal Employer employer) {

        try {
            employerProfileService.deleteEmployerLogo(employer.getId());
            return ResponseEntity.ok(Map.of("message", "Logo deleted successfully"));

        } catch (Exception e) {
            log.error("Error deleting logo for employer {}: {}", employer.getId(), e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Failed to delete logo"));
        }
    }
}