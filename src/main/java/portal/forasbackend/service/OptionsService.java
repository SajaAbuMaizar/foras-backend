package portal.forasbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import portal.forasbackend.dto.response.options.OptionsResponse;
import portal.forasbackend.entity.City;
import portal.forasbackend.entity.Industry;
import portal.forasbackend.domain.job.model.JobType;
import portal.forasbackend.repository.CityRepository;
import portal.forasbackend.repository.IndustryRepository;
import portal.forasbackend.domain.job.repository.JobTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionsService {

    private final CityRepository cityRepository;
    private final IndustryRepository industryRepository;
    private final JobTypeRepository jobTypeRepository;

    public OptionsResponse getAllOptions() {
        List<City> cities = cityRepository.findAll();
        List<Industry> industries = industryRepository.findAll();
        List<JobType> jobTypes = jobTypeRepository.findAll();

        return OptionsResponse.builder()
                .cities(cities)
                .industries(industries)
                .jobTypes(jobTypes)
                .build();
    }
}