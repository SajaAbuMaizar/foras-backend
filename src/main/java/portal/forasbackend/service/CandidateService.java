package portal.forasbackend.service;

import org.springframework.stereotype.Service;
import portal.forasbackend.entity.Candidate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import portal.forasbackend.repository.CandidateRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    public Optional<Candidate> findById(Long candidateId) {
        return candidateRepository.findById(candidateId);
    }


    public Optional<Candidate> findByPhone(String phone) {
        return candidateRepository.findByPhone(phone);
    }
}