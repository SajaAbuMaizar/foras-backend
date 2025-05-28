package portal.forasbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import portal.forasbackend.entity.City;
import portal.forasbackend.repository.CityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}