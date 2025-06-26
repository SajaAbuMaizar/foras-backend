package portal.forasbackend.controller.Employer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import portal.forasbackend.dto.request.employer.LanguageChangeRequest;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.service.Employer.EmployerService;

import java.util.Optional;

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

    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping("/change-lang")
    public ResponseEntity<?> changeLanguage(@RequestBody LanguageChangeRequest request, @AuthenticationPrincipal Employer employer) {
        employer.setPreferredLanguage(request.getLang());
        employerService.save(employer);
        return ResponseEntity.ok().build();
    }


}