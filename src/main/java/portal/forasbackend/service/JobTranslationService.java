package portal.forasbackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.domain.model.Admin;
import portal.forasbackend.domain.model.Job;
import portal.forasbackend.domain.model.JobTranslation;
import portal.forasbackend.domain.repository.JobRepository;
import portal.forasbackend.domain.repository.JobTranslationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobTranslationService {

    private final JobTranslationRepository translationRepository;
    private final JobRepository jobRepository;

    public void createOriginal(Job job, String language, String title, String description, String requiredQualifications) {
        JobTranslation translation = JobTranslation.builder()
                .job(job)
                .language(language)
                .title(title)
                .description(description)
                .requiredQualifications(requiredQualifications)
                .isOriginal(true)
                .createdAt(LocalDateTime.now())
                .build();

        translationRepository.save(translation);
    }

    @Transactional
    public void upsertArabicTranslation(Long jobId, String title, String description, String qualifications) {
        Job job = jobRepository.findByIdWithTranslations(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id " + jobId));

        Optional<JobTranslation> arabicTranslation = job.getTranslations().stream()
                .filter(t -> "ar".equals(t.getLanguage()) && !t.isOriginal())
                .findFirst();

        if (arabicTranslation.isPresent()) {
            JobTranslation tr = arabicTranslation.get();
            tr.setTitle(title);
            tr.setDescription(description);
            tr.setRequiredQualifications(qualifications);
        } else {
            JobTranslation tr = JobTranslation.builder()
                    .job(job)
                    .language("ar")
                    .title(title)
                    .description(description)
                    .requiredQualifications(qualifications)
                    .isOriginal(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            job.getTranslations().add(tr);
            translationRepository.save(tr);
        }
    }


    public JobTranslation createTranslation(Job job, String language, String title, String description) {
        JobTranslation translation = JobTranslation.builder()
                .job(job)
                .language(language)
                .title(title)
                .description(description)
                .isOriginal(false)
                .createdAt(LocalDateTime.now())
                .build();

        return translationRepository.save(translation);
    }

//    @Transactional
//    public JobTranslation approveTranslation(Long translationId, Admin admin) {
//        JobTranslation translation = translationRepository.findById(translationId)
//                .orElseThrow(() -> new EntityNotFoundException("Translation not found"));
//
//        translation.setVerified(true);
//        translation.setVerifiedAt(LocalDateTime.now());
//        translation.setVerifiedBy(admin);
//
//        return translation;
//    }

    public Optional<JobTranslation> getArabicTranslation(Long jobId) {
        return translationRepository.findByJobIdAndLanguage(jobId, "ar");
    }


    public List<JobTranslation> getAllTranslationsForJob(Long jobId) {
        return translationRepository.findByJobId(jobId);
    }

    @Transactional
    public void save(JobTranslation translation) {
        translationRepository.save(translation);
    }

}
