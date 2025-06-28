package portal.forasbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.entity.City;
import portal.forasbackend.repository.CityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CityService {

    private final CityRepository cityRepository;

    @Cacheable(value = "cities", unless = "#result.isEmpty()")
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}