package portal.forasbackend.controller.Employer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.service.Employer.EmployerService;

@RestController
@RequestMapping("/api/employer")
@RequiredArgsConstructor
public class EmployerController {
    private final EmployerService employerService;

    @PostMapping("/upload-logo")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<?> uploadLogo(
            @RequestParam("logo") MultipartFile file,
            @AuthenticationPrincipal Employer employer) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        employerService.saveLogoForEmployer(employer.getPhone(), file);
        return ResponseEntity.ok().body("Logo uploaded successfully");
    }
}