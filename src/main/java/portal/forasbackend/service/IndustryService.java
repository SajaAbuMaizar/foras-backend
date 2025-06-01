package portal.forasbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import portal.forasbackend.entity.Industry;
import portal.forasbackend.repository.IndustryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndustryService {

    private final IndustryRepository industryRepository;

    public List<Industry> getAllIndustries() {
        return industryRepository.findAll();
    }
}
