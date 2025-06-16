package portal.forasbackend.service.Candidate;

import org.springframework.stereotype.Service;
import portal.forasbackend.dto.response.candidate.CandidateProfileDto;
import portal.forasbackend.entity.Candidate;
import lombok.RequiredArgsConstructor;
import portal.forasbackend.exception.business.NotFoundException;
import portal.forasbackend.mapper.CandidateMapper;
import portal.forasbackend.repository.CandidateRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;

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
}
