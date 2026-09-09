package com.trainingcenter.controller;

import com.trainingcenter.dto.request.TrainerRequest;
import com.trainingcenter.dto.response.ApiResponse;
import com.trainingcenter.dto.response.TrainerResponse;
import com.trainingcenter.repository.UserAccountRepository;
import com.trainingcenter.security.UserPrincipal;
import com.trainingcenter.service.TrainerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/trainers")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TrainerResponse>>> searchTrainers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long centerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (auth != null && auth.getPrincipal() instanceof UserPrincipal)
                ? (UserPrincipal) auth.getPrincipal()
                : null;
        List<String> roles = principal != null ? principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()) : List.of();

        // Center Manager can only search their center's trainers
        if (principal != null && roles.contains("ROLE_CENTER_MANAGER")) {
            centerId = getCenterIdForUserAccount(principal);
        }

        Page<TrainerResponse> trainers = trainerService.searchTrainers(query, status, centerId, page, size);
        return ResponseEntity.ok(ApiResponse.success("Trainers fetched successfully", trainers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainerResponse>> getTrainerById(
            @PathVariable Long id,
            Authentication authentication) {

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        TrainerResponse trainer = trainerService.getTrainerById(id);

        if (roles.contains("ROLE_TRAINER")) {
            if (!trainer.getUserId().equals(principal.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied: You can only access your own profile"));
            }
        } else if (roles.contains("ROLE_CENTER_MANAGER")) {
            Long managerCenterId = getCenterIdForUserAccount(principal);
            if (!trainer.getCenterId().equals(managerCenterId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied: Trainer belongs to another center"));
            }
        }

        return ResponseEntity.ok(ApiResponse.success("Trainer profile fetched successfully", trainer));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TrainerResponse>> createTrainer(@Valid @RequestBody TrainerRequest request) {
        TrainerResponse trainer = trainerService.createTrainer(request);
        return ResponseEntity.ok(ApiResponse.success("Trainer registered successfully", trainer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainerResponse>> updateTrainer(
            @PathVariable Long id,
            @Valid @RequestBody TrainerRequest request) {
        TrainerResponse trainer = trainerService.updateTrainer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Trainer updated successfully", trainer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return ResponseEntity.ok(ApiResponse.success("Trainer deleted successfully", "Deleted trainer with ID: " + id));
    }

    private Long getCenterIdForUserAccount(UserPrincipal principal) {
        return userAccountRepository.findById(principal.getId())
                .map(user -> user.getCenter() != null ? user.getCenter().getCenterId() : null)
                .orElse(null);
    }

}
