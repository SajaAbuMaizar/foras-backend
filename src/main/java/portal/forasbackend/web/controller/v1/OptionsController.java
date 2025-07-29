package portal.forasbackend.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portal.forasbackend.dto.response.options.OptionsResponse;
import portal.forasbackend.service.OptionsService;

@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
public class OptionsController {
    private final OptionsService optionsService;

    @GetMapping
    public ResponseEntity<OptionsResponse> getAllOptions() {
        OptionsResponse options = optionsService.getAllOptions();
        return ResponseEntity.ok(options);
    }
}
