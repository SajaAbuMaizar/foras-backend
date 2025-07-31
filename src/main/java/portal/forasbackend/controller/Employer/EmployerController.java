package portal.forasbackend.controller.Employer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.employer.LanguageChangeRequest;
import portal.forasbackend.dto.response.FileUploadResponse;
import portal.forasbackend.domain.employer.model.Employer;
import portal.forasbackend.exception.FileUploadException;
import portal.forasbackend.domain.employer.service.EmployerService;

@Slf4j
@RestController
@RequestMapping("/api/employer")
@RequiredArgsConstructor
public class EmployerController {
    private final EmployerService employerService;

    @PostMapping("/upload-logo")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<FileUploadResponse> uploadLogo(
            @RequestParam("logo") MultipartFile file,
            @AuthenticationPrincipal Employer employer) {
        log.info("Uploading logo for employer: {}", employer.getCompanyName());

        try {
            String logoUrl = employerService.saveLogoForEmployer(employer.getPhone(), file);
            return ResponseEntity.ok(FileUploadResponse.builder()
                    .success(true)
                    .url(logoUrl)
                    .message("تم رفع الشعار بنجاح")
                    .build());
        } catch (FileUploadException e) {
            log.error("File upload error for employer {}: {}", employer.getCompanyName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(FileUploadResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .errorCode(e.getErrorCode())
                            .build());
        } catch (Exception e) {
            log.error("Unexpected error uploading logo for employer {}", employer.getCompanyName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(FileUploadResponse.builder()
                            .success(false)
                            .message("حدث خطأ غير متوقع. يرجى المحاولة مرة أخرى")
                            .errorCode("UNEXPECTED_ERROR")
                            .build());
        }
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping("/change-lang")
    public ResponseEntity<?> changeLanguage(@RequestBody LanguageChangeRequest request, @AuthenticationPrincipal Employer employer) {
        employer.setPreferredLanguage(request.getLang());
        employerService.save(employer);
        return ResponseEntity.ok().build();
    }
}