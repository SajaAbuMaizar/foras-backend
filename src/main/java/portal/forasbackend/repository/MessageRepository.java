package portal.forasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portal.forasbackend.entity.Message;

import java.time.LocalDateTime;

public interface MessageRepository extends JpaRepository<Message, Long> {
    long countByCreatedAtAfter(LocalDateTime after);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    long countByReadFalse();
    long countBySentTrue();
    long countByArchivedTrue();
    long countByDeletedTrue();
    long countByRespondedTrue();
}