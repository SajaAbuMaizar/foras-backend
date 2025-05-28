package portal.forasbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.dto.request.user.UserSignupRequestDTO;
import portal.forasbackend.dto.response.user.UserSignupResponseDTO;
import portal.forasbackend.entity.City;
import portal.forasbackend.entity.User;
import portal.forasbackend.enums.Gender;
import portal.forasbackend.exception.business.CityNotFoundException;
import portal.forasbackend.exception.business.InvalidGenderException;
import portal.forasbackend.exception.business.PhoneExistsException;
import portal.forasbackend.repository.CityRepository;
import portal.forasbackend.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserSignupResponseDTO registerUser(UserSignupRequestDTO request) {
        // Validate phone number
        if (userRepository.existsByPhone(request.getPhone())) {
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

        // Create and save user
        User savedUser = userRepository.save(
                User.builder()
                        .name(request.getName())
                        .phone(request.getPhone())
                        .city(city)
                        .gender(Gender.valueOf(request.getGender()))
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build()
        );

        return new UserSignupResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getPhone()
        );
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }


    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }
}