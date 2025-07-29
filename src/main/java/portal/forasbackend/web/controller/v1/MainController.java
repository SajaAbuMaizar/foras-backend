package portal.forasbackend.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portal.forasbackend.dto.response.employer.EmployerLogoUrlDTO;
import portal.forasbackend.service.Employer.EmployerService;
import java.util.List;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    private final EmployerService employerService;

    @GetMapping("/logos")
    public ResponseEntity<List<EmployerLogoUrlDTO>> getAllEmployerLogos() {
        return ResponseEntity.ok(employerService.getAllCompanyLogos());
    }

}