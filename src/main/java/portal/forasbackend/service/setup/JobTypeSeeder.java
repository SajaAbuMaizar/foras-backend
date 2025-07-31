package portal.forasbackend.service.setup;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.domain.job.model.JobType;
import portal.forasbackend.domain.job.repository.JobTypeRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobTypeSeeder {

    private final JobTypeRepository jobTypeRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void seedJobTypes() {
        if (jobTypeRepository.count() > 0) {
            log.info("Job types already seeded. Skipping...");
            return;
        }

        try {
            ClassPathResource resource = new ClassPathResource("data/job-types.json");
            InputStream inputStream = resource.getInputStream();

            List<JobType> jobTypes = objectMapper.readValue(
                    inputStream,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, JobType.class)
            );

            jobTypeRepository.saveAll(jobTypes);
            log.info("Successfully seeded {} work types", jobTypes.size());

        } catch (IOException e) {
            log.error("Error reading work-types.json", e);
            throw new RuntimeException("Failed to seed work types", e);
        }
    }
}