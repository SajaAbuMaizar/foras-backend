package portal.forasbackend.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import portal.forasbackend.entity.Industry;
import portal.forasbackend.repository.IndustryRepository;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IndustryDataLoader implements CommandLineRunner {

    private final IndustryRepository industryRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        if (industryRepository.count() == 0) {
            InputStream inputStream = getClass().getResourceAsStream("/data/industries.json");
            List<Industry> industries = objectMapper.readValue(inputStream, new TypeReference<>() {});
            industryRepository.saveAll(industries);
            System.out.println("✔ Loaded " + industries.size() + " industries from JSON.");
        }
    }
}