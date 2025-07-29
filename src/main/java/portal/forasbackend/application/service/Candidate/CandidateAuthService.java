package portal.forasbackend.application.service.Candidate;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.application.dto.request.candidate.CandidateSignupRequestDTO;
import portal.forasbackend.application.dto.response.candidate.CandidateSignupResponseDTO;
import portal.forasbackend.domain.model.City;
import portal.forasbackend.domain.model.Candidate;
import portal.forasbackend.domain.enums.Gender;
import portal.forasbackend.core.exception.business.CityNotFoundException;
import portal.forasbackend.core.exception.business.InvalidGenderException;
import portal.forasbackend.core.exception.business.PhoneExistsException;
import portal.forasbackend.domain.repository.CityRepository;
import portal.forasbackend.domain.repository.CandidateRepository;
import portal.forasbackend.application.service.JwtService;


@Service
@RequiredArgsConstructor
public class CandidateAuthService {

    private final CandidateRepository candidateRepository;
    private final CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Transactional
    public String registerCandidate(CandidateSignupRequestDTO request) {
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

        return jwtService.generateToken(savedCandidate);
    }
}