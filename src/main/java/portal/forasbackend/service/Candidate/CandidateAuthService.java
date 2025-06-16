package portal.forasbackend.service.Candidate;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.dto.request.candidate.CandidateSignupRequestDTO;
import portal.forasbackend.dto.response.candidate.CandidateSignupResponseDTO;
import portal.forasbackend.entity.City;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.enums.Gender;
import portal.forasbackend.exception.business.CityNotFoundException;
import portal.forasbackend.exception.business.InvalidGenderException;
import portal.forasbackend.exception.business.PhoneExistsException;
import portal.forasbackend.repository.CityRepository;
import portal.forasbackend.repository.CandidateRepository;


@Service
@RequiredArgsConstructor
public class CandidateAuthService {

    private final CandidateRepository candidateRepository;
    private final CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CandidateSignupResponseDTO registerCandidate(CandidateSignupRequestDTO request) {
        // Validate phone number
        if (candidateRepository.existsByPhone(request.getPhone())) {
            throw new PhoneExistsException(request.getPhone());
        }

        // Validate city
        City city = cityRepository.findByCode(request.getCity())
                .orElseThrow(() -> new CityNotFoundException(request.getCity()));

        // Validate gender
        try {
            Gender.valueOf(request.getGender().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidGenderException(request.getGender());
        }

        // Create and save candidate
        Candidate savedCandidate = candidateRepository.save(
                Candidate.builder()
                        .name(request.getName())
                        .phone(request.getPhone())
                        .city(city)
                        .gender(Gender.valueOf(request.getGender()))
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build()
        );

        return new CandidateSignupResponseDTO(
                savedCandidate.getId(),
                savedCandidate.getName(),
                savedCandidate.getPhone()
        );
    }
}