package portal.forasbackend.service.Candidate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.candidate.UpdateCandidateProfileRequest;
import portal.forasbackend.dto.response.candidate.CandidateProfileDto;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.exception.business.NotFoundException;
import portal.forasbackend.mapper.CandidateMapper;
import portal.forasbackend.repository.CandidateRepository;
import portal.forasbackend.service.CloudinaryService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;
    private final CloudinaryService cloudinaryService;

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

        // Update fields
        candidate.setName(request.getName());
        candidate.setGender(request.getGender());
        candidate.setKnowsHebrew(request.getKnowsHebrew());
        candidate.setNeedsHelp(request.getNeedsHelp());

        // Update collections
        candidate.setSkills(request.getSkills());
        candidate.setLanguages(request.getLanguages());
        candidate.setDriverLicenses(request.getDriverLicenses());

        Candidate savedCandidate = candidateRepository.save(candidate);
        log.info("Updated profile for candidate: {}", candidateId);

        return candidateMapper.toDto(savedCandidate);
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