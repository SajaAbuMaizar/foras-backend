package portal.forasbackend.web.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portal.forasbackend.domain.enums.Gender;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enums")
public class EnumController {

    @GetMapping("/genders")
    public List<Map<String, String>> getGenders() {
        return Arrays.stream(Gender.values())
                .map(gender -> Map.of(
                        "code", gender.name(),
                        "id", gender.getId(),
                        "nameAr", gender.getArabic(),
                        "nameHe", gender.getHebrew()
                ))
                .toList();
    }
}