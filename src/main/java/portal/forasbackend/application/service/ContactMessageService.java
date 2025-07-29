package portal.forasbackend.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.application.dto.request.ContactMessageRequest;
import portal.forasbackend.application.dto.request.UpdateContactMessageRequest;
import portal.forasbackend.application.dto.response.ContactMessageResponse;
import portal.forasbackend.application.dto.response.admin.ContactMessageStatsResponse;
import portal.forasbackend.domain.model.Admin;
import portal.forasbackend.domain.model.ContactMessage;
import portal.forasbackend.domain.model.ContactMessage.ContactMessageStatus;
import portal.forasbackend.core.exception.NotFoundException;
import portal.forasbackend.domain.repository.ContactMessageRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    @Transactional
    public ContactMessageResponse createContactMessage(ContactMessageRequest request) {
        log.info("Creating new contact message from: {}", request.getFullName());

        ContactMessage message = ContactMessage.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status(ContactMessageStatus.NEW)
                .read(false)
                .build();

        ContactMessage saved = contactMessageRepository.save(message);
        log.info("Contact message created with ID: {}", saved.getId());

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<ContactMessageResponse> getAllMessages(Pageable pageable) {
        return contactMessageRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ContactMessageResponse> getMessagesByStatus(ContactMessageStatus status, Pageable pageable) {
        return contactMessageRepository.findByStatus(status, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ContactMessageResponse> searchMessages(ContactMessageStatus status, String search, Pageable pageable) {
        return contactMessageRepository.searchMessages(status, search, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public ContactMessageResponse getMessageById(Long id) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Contact message not found with id: " + id));
        return mapToResponse(message);
    }

    @Transactional
    public ContactMessageResponse markAsRead(Long id) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Contact message not found with id: " + id));

        if (!message.isRead()) {
            message.setRead(true);
            message = contactMessageRepository.save(message);
            log.info("Contact message {} marked as read", id);
        }

        return mapToResponse(message);
    }

    @Transactional
    public ContactMessageResponse updateMessageStatus(Long id, UpdateContactMessageRequest request, Admin admin) {
        log.info("Updating contact message {} status to {} by admin {}", id, request.getStatus(), admin.getId());

        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Contact message not found with id: " + id));

        message.setStatus(request.getStatus());

        if (request.getAdminNotes() != null) {
            message.setAdminNotes(request.getAdminNotes());
        }

        if (!message.isRead()) {
            message.setRead(true);
        }

        if (request.getStatus() == ContactMessageStatus.DONE) {
            message.setHandledBy(admin.getId());
            message.setHandledAt(LocalDateTime.now());
        }

        ContactMessage updated = contactMessageRepository.save(message);
        log.info("Contact message {} updated successfully", id);

        return mapToResponse(updated);
    }

    @Transactional(readOnly = true)
    public ContactMessageStatsResponse getMessageStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime weekStart = now.minusDays(7);
        LocalDateTime monthStart = now.minusDays(30);

        return ContactMessageStatsResponse.builder()
                .totalMessages(contactMessageRepository.count())
                .newMessages(contactMessageRepository.countByStatus(ContactMessageStatus.NEW))
                .inProgressMessages(contactMessageRepository.countByStatus(ContactMessageStatus.IN_PROGRESS))
                .doneMessages(contactMessageRepository.countByStatus(ContactMessageStatus.DONE))
                .archivedMessages(contactMessageRepository.countByStatus(ContactMessageStatus.ARCHIVED))
                .unreadMessages(contactMessageRepository.countByReadFalse())
                .todayMessages(contactMessageRepository.countByCreatedAtAfter(todayStart))
                .weekMessages(contactMessageRepository.countByCreatedAtAfter(weekStart))
                .monthMessages(contactMessageRepository.countByCreatedAtAfter(monthStart))
                .build();
    }

    @Transactional(readOnly = true)
    public List<ContactMessageResponse> getRecentMessages(int limit) {
        return contactMessageRepository.findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ContactMessageResponse mapToResponse(ContactMessage message) {
        return ContactMessageResponse.builder()
                .id(message.getId())
                .fullName(message.getFullName())
                .phoneNumber(message.getPhoneNumber())
                .subject(message.getSubject())
                .message(message.getMessage())
                .status(message.getStatus())
                .adminNotes(message.getAdminNotes())
                .read(message.isRead())
                .handledBy(message.getHandledBy())
                .handledAt(message.getHandledAt())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}