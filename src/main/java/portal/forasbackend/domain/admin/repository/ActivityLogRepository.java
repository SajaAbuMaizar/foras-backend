package portal.forasbackend.domain.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import portal.forasbackend.domain.admin.model.ActivityLog;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    @Query("SELECT a FROM ActivityLog a ORDER BY a.createdAt DESC")
    List<ActivityLog> findTopNByOrderByCreatedAtDesc(Pageable pageable);
}