package portal.forasbackend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portal.forasbackend.enums.Gender;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enums")
@CrossOrigin(origins = "*")
public class EnumController {

    @GetMapping("/genders")
    public List<Map<String, String>> getGenders() {
        return Arrays.stream(Gender.values())
                .map(sex -> Map.of(
                        "value", sex.name(),
                        "labelAr", sex.getArabic(),
                        "labelHe", sex.getHebrew()
                ))
                .toList();
    }
}