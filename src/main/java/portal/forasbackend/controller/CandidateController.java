package portal.forasbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.dto.request.candidate.CandidateSignupRequestDTO;
import portal.forasbackend.dto.response.candidate.CandidateSignupResponseDTO;
import portal.forasbackend.service.CandidateAuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/candidate")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CandidateController {

    private final CandidateAuthService candidateService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> registerCandidate(
            @RequestBody CandidateSignupRequestDTO request
    ) {
        CandidateSignupResponseDTO candidate = candidateService.registerCandidate(request);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", candidate
        ));
    }
}