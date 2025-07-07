package portal.forasbackend.controller.Admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.dto.request.UpdateContactMessageRequest;
import portal.forasbackend.dto.response.ContactMessageResponse;
import portal.forasbackend.dto.response.admin.ContactMessageStatsResponse;
import portal.forasbackend.entity.Admin;
import portal.forasbackend.entity.ContactMessage.ContactMessageStatus;
import portal.forasbackend.service.ContactMessageService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/contact-messages")
@RequiredArgsConstructor
@Tag(name = "Admin Contact Messages", description = "Admin contact message management endpoints")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminContactMessageController {

    private final ContactMessageService contactMessageService;

    @GetMapping
    @Operation(summary = "Get all contact messages", description = "Get paginated list of contact messages")
    public ResponseEntity<Page<ContactMessageResponse>> getAllMessages(
            @RequestParam(required = false) ContactMessageStatus status,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("Admin fetching contact messages - status: {}, search: {}", status, search);

        Page<ContactMessageResponse> messages;
        if (search != null && !search.trim().isEmpty()) {
            messages = contactMessageService.searchMessages(status, search, pageable);
        } else if (status != null) {
            messages = contactMessageService.getMessagesByStatus(status, pageable);
        } else {
            messages = contactMessageService.getAllMessages(pageable);
        }

        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get contact message by ID", description = "Get detailed contact message")
    public ResponseEntity<ContactMessageResponse> getMessageById(@PathVariable Long id) {
        log.info("Admin fetching contact message: {}", id);
        ContactMessageResponse message = contactMessageService.getMessageById(id);
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark message as read", description = "Mark contact message as read")
    public ResponseEntity<ContactMessageResponse> markAsRead(@PathVariable Long id) {
        log.info("Admin marking message {} as read", id);
        ContactMessageResponse message = contactMessageService.markAsRead(id);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update message status", description = "Update contact message status and notes")
    public ResponseEntity<ContactMessageResponse> updateMessageStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateContactMessageRequest request,
            @AuthenticationPrincipal Admin admin) {

        log.info("Admin {} updating message {} status to {}", admin.getId(), id, request.getStatus());
        ContactMessageResponse updated = contactMessageService.updateMessageStatus(id, request, admin);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get message statistics", description = "Get contact message statistics")
    public ResponseEntity<ContactMessageStatsResponse> getMessageStats() {
        log.info("Admin fetching contact message statistics");
        ContactMessageStatsResponse stats = contactMessageService.getMessageStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent messages", description = "Get recent contact messages for dashboard")
    public ResponseEntity<List<ContactMessageResponse>> getRecentMessages(
            @RequestParam(defaultValue = "5") int limit) {

        log.info("Admin fetching recent {} contact messages", limit);
        List<ContactMessageResponse> messages = contactMessageService.getRecentMessages(limit);
        return ResponseEntity.ok(messages);
    }
}