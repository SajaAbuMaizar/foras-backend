package portal.forasbackend.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import portal.forasbackend.entity.City;
import portal.forasbackend.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CityDataLoader implements CommandLineRunner {

    private final CityRepository cityRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        if (cityRepository.count() == 0) {
            InputStream inputStream = getClass().getResourceAsStream("/data/cities.json");
            List<City> cities = objectMapper.readValue(inputStream, new TypeReference<>() {});
            cityRepository.saveAll(cities);
            System.out.println("✔ Loaded " + cities.size() + " cities from JSON.");
        }
    }
}
