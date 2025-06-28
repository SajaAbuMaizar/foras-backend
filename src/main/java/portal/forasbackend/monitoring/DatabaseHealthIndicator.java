package portal.forasbackend.monitoring;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import portal.forasbackend.repository.JobRepository;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;
    private final JobRepository jobRepository;

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                long jobCount = jobRepository.count();
                return Health.up()
                        .withDetail("database", "PostgreSQL")
                        .withDetail("total_jobs", jobCount)
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withException(e)
                    .build();
        }
        return Health.down().build();
    }
}
