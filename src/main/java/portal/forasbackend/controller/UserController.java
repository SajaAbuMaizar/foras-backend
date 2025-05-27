package portal.forasbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.dto.request.user.UserSignupRequestDTO;
import portal.forasbackend.dto.response.user.UserSignupResponseDTO;
import portal.forasbackend.entity.User;
import portal.forasbackend.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> registerUser(
            @RequestBody UserSignupRequestDTO request
    ) {
        UserSignupResponseDTO user = userService.registerUser(request);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "User registered successfully",
                "data", user
        ));
    }
}