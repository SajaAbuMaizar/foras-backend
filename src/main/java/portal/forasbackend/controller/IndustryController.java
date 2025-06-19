package portal.forasbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.entity.Industry;
import portal.forasbackend.service.IndustryService;


import java.util.List;

@RestController
@RequestMapping("/api/industries")
@RequiredArgsConstructor
public class IndustryController {

    private final IndustryService industryService;

    @GetMapping
    public List<Industry> getAllIndustries() {
        return industryService.getAllIndustries();
    }
}