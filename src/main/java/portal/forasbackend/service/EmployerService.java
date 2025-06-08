package portal.forasbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.repository.EmployerRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployerService {

    @Autowired
    private EmployerRepository employerRepository;

    public Optional<Employer> findById(Long employerId) {
        return employerRepository.findById(employerId);
    }

    public Optional<Employer> findByPhone(String phone) {
        return employerRepository.findByPhone(phone);
    }
}

