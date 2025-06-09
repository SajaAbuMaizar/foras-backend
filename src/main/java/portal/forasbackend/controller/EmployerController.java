package portal.forasbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.service.EmployerService;

import java.security.Principal;

@RestController
@RequestMapping("/api/employer")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmployerController {
    private final EmployerService employerService;

    @PostMapping("/upload-logo")
    public ResponseEntity<?> uploadLogo(@RequestParam("logo") MultipartFile file, @AuthenticationPrincipal Employer employer) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        String employerPhone = employer.getPhone();
        employerService.saveLogoForEmployer(employerPhone, file);

        return ResponseEntity.ok().body("Logo uploaded successfully");
    }

}
