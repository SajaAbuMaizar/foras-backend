package portal.forasbackend.application.service.Candidate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.application.dto.request.candidate.UpdateCandidateProfileRequest;
import portal.forasbackend.application.dto.request.candidate.UpdateCredentialsRequest;
import portal.forasbackend.application.dto.response.candidate.CandidateProfileDto;
import portal.forasbackend.domain.model.Candidate;
import portal.forasbackend.core.exception.business.NotFoundException;
import portal.forasbackend.application.mapper.CandidateMapper;
import portal.forasbackend.domain.repository.CandidateRepository;
import portal.forasbackend.application.service.CloudinaryService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;
    private final CloudinaryService cloudinaryService;
    private final BCryptPasswordEncoder passwordEncoder;

    public Optional<Candidate> findById(Long candidateId) {
        return candidateRepository.findById(candidateId);
    }

    public Optional<Candidate> findByPhone(String phone) {
        return candidateRepository.findByPhone(phone);
    }

    public CandidateProfileDto getCandidateDetails(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate not found"));
        return candidateMapper.toDto(candidate);
    }

    @Transactional
    public CandidateProfileDto updateCandidateProfile(Long candidateId, UpdateCandidateProfileRequest request) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate not found"));

        candidate.setName(request.getName());
        candidate.setGender(request.getGender());
        candidate.setKnowsHebrew(request.getKnowsHebrew());
        candidate.setNeedsHelp(request.getNeedsHelp());
        candidate.setSkills(request.getSkills());
        candidate.setLanguages(request.getLanguages());
        candidate.setDriverLicenses(request.getDriverLicenses());

        Candidate savedCandidate = candidateRepository.save(candidate);
        log.info("Updated profile for candidate: {}", candidateId);

        return candidateMapper.toDto(savedCandidate);
    }

    @Transactional
    public void updateCredentials(Long candidateId, UpdateCredentialsRequest request) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate not found"));

        candidate.setName(request.getName());

        if (request.getCurrentPassword() != null && request.getNewPassword() != null) {
            if (!passwordEncoder.matches(request.getCurrentPassword(), candidate.getPassword())) {
                throw new IllegalArgumentException("كلمة المرور الحالية غير صحيحة");
            }
            candidate.setPassword(passwordEncoder.encode(request.getNewPassword()));
            log.info("Updated password for candidate: {}", candidateId);
        }

        candidateRepository.save(candidate);
        log.info("Updated credentials for candidate: {}", candidateId);
    }

    @Transactional
    public String updateCandidateAvatar(Long candidateId, MultipartFile file) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate not found"));

        String avatarUrl = cloudinaryService.uploadCandidateAvatar(file);
        candidate.setAvatarUrl(avatarUrl);

        candidateRepository.save(candidate);
        log.info("Updated avatar for candidate: {}", candidateId);

        return avatarUrl;
    }
}