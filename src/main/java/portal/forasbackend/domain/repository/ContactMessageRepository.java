package portal.forasbackend.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import portal.forasbackend.domain.model.ContactMessage;
import portal.forasbackend.domain.model.ContactMessage.ContactMessageStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    Page<ContactMessage> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<ContactMessage> findByStatus(ContactMessageStatus status, Pageable pageable);

    long countByReadFalse();

    long countByStatus(ContactMessageStatus status);

    long countByCreatedAtAfter(LocalDateTime after);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT cm FROM ContactMessage cm WHERE " +
            "(:status IS NULL OR cm.status = :status) AND " +
            "(:search IS NULL OR LOWER(cm.fullName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(cm.phoneNumber) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(cm.subject) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(cm.message) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ContactMessage> searchMessages(@Param("status") ContactMessageStatus status,
                                        @Param("search") String search,
                                        Pageable pageable);

    List<ContactMessage> findTop5ByOrderByCreatedAtDesc();
}