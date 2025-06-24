package portal.forasbackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.entity.Admin;
import portal.forasbackend.entity.Job;
import portal.forasbackend.entity.JobTranslation;
import portal.forasbackend.repository.JobTranslationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobTranslationService {

    private final JobTranslationRepository translationRepository;

    public void createOriginal(Job job, String language, String title, String description, String requiredQualifications) {
        JobTranslation translation = JobTranslation.builder()
                .job(job)
                .language(language)
                .title(title)
                .description(description)
                .RequiredQualifications(requiredQualifications)
                .isOriginal(true)
                .isVerified(false)
                .createdAt(LocalDateTime.now())
                .build();

        translationRepository.save(translation);
    }

    public JobTranslation createTranslation(Job job, String language, String title, String description) {
        JobTranslation translation = JobTranslation.builder()
                .job(job)
                .language(language)
                .title(title)
                .description(description)
                .isOriginal(false)
                .isVerified(false)
                .createdAt(LocalDateTime.now())
                .build();

        return translationRepository.save(translation);
    }

    @Transactional
    public JobTranslation approveTranslation(Long translationId, Admin admin) {
        JobTranslation translation = translationRepository.findById(translationId)
                .orElseThrow(() -> new EntityNotFoundException("Translation not found"));

        translation.setVerified(true);
        translation.setVerifiedAt(LocalDateTime.now());
        translation.setVerifiedBy(admin);

        return translation;
    }

    public Optional<JobTranslation> getVerifiedArabicTranslation(Long jobId) {
        return translationRepository.findByJobIdAndLanguage(jobId, "ar")
                .filter(JobTranslation::isVerified);
    }

    public List<JobTranslation> getAllTranslationsForJob(Long jobId) {
        return translationRepository.findByJobId(jobId);
    }
}
