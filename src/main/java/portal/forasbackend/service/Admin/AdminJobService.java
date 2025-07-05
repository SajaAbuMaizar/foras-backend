package portal.forasbackend.service.Admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.dto.LocalizedNameDto;
import portal.forasbackend.dto.response.admin.JobListResponse;
import portal.forasbackend.repository.JobRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminJobService {

    private final JobRepository jobRepository;

    public List<JobListResponse> getRecentJobs(int limit) {
        log.info("Fetching {} recent jobs", limit);

        return jobRepository.findAllByOrderByCreatedAtDesc().stream()
                .limit(limit)
                .map(job -> {
                    // Get the first translation as "original"
                    var translation = job.getTranslations() != null && !job.getTranslations().isEmpty()
                            ? job.getTranslations().get(0)
                            : null;
                    return JobListResponse.builder()
                            .id(job.getId().toString())
                            .jobTitle(translation != null ? translation.getTitle() : null)
                            .cityName(LocalizedNameDto.from(job.getCity()))
                            .jobDescription(translation != null ? translation.getDescription() : null)
                            .salary(job.getSalary())
                            .status(job.getStatus().name())
                            .build();
                })
                .collect(Collectors.toList());
    }
}