package portal.forasbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.dto.request.ContactMessageRequest;
import portal.forasbackend.dto.response.ContactMessageResponse;
import portal.forasbackend.service.ContactMessageService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactMessageService contactMessageService;

    @PostMapping("/send-message")
    public ResponseEntity<?> sendContactMessage(@Valid @RequestBody ContactMessageRequest request) {
        try {
            log.info("Received contact message from: {}", request.getFullName());

            ContactMessageResponse response = contactMessageService.createContactMessage(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "تم إرسال رسالتك بنجاح. سنتواصل معك قريباً",
                    "id", response.getId()
            ));
        } catch (Exception e) {
            log.error("Error sending contact message: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "حدث خطأ أثناء إرسال الرسالة. يرجى المحاولة مرة أخرى"
            ));
        }
    }
}