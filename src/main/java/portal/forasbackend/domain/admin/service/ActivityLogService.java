package portal.forasbackend.domain.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.domain.admin.model.ActivityLog;
import portal.forasbackend.domain.admin.repository.ActivityLogRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Transactional
    public void logActivity(String type, String description, Long userId, String userName) {
        log.info("Logging activity: {} - {}", type, description);

        ActivityLog activityLog = ActivityLog.builder()
                .activityType(type)
                .description(description)
                .userId(userId)
                .userName(userName)
                .build();

        activityLogRepository.save(activityLog);
    }

    @Transactional
    public void logEntityActivity(String type, String description, Long userId, String userName,
                                  Long entityId, String entityType) {
        log.info("Logging entity activity: {} - {} for entity: {} ({})",
                type, description, entityId, entityType);

        ActivityLog activityLog = ActivityLog.builder()
                .activityType(type)
                .description(description)
                .userId(userId)
                .userName(userName)
                .entityId(entityId)
                .entityType(entityType)
                .build();

        activityLogRepository.save(activityLog);
    }
}