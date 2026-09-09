package com.trainingcenter.controller;

import com.trainingcenter.dto.request.BatchRequest;
import com.trainingcenter.dto.response.ApiResponse;
import com.trainingcenter.dto.response.BatchResponse;
import com.trainingcenter.repository.UserAccountRepository;
import com.trainingcenter.security.UserPrincipal;
import com.trainingcenter.service.BatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/batches")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BatchResponse>>> getBatches(
            @RequestParam(required = false) Long centerId) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
            List<String> roles = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            if (roles.contains("ROLE_CENTER_MANAGER")) {
                centerId = getCenterIdForUserAccount(principal);
            }
        }

        List<BatchResponse> batches = batchService.getBatchesByCenter(centerId);
        return ResponseEntity.ok(ApiResponse.success("Batches fetched successfully", batches));
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<BatchResponse>>> getBatchesPaginated(
            @RequestParam(required = false) Long centerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
            List<String> roles = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            if (roles.contains("ROLE_CENTER_MANAGER")) {
                centerId = getCenterIdForUserAccount(principal);
            }
        }

        Page<BatchResponse> batches = batchService.getBatchesByCenterPaginated(centerId, page, size);
        return ResponseEntity.ok(ApiResponse.success("Batches fetched successfully", batches));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BatchResponse>> getBatchById(@PathVariable Long id) {
        BatchResponse batch = batchService.getBatchById(id);
        return ResponseEntity.ok(ApiResponse.success("Batch fetched successfully", batch));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CENTER_MANAGER')")
    public ResponseEntity<ApiResponse<BatchResponse>> createBatch(@Valid @RequestBody BatchRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        if (principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CENTER_MANAGER"))) {
            request.setCenterId(getCenterIdForUserAccount(principal));
        }

        BatchResponse batch = batchService.createBatch(request);
        return ResponseEntity.ok(ApiResponse.success("Batch created successfully", batch));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CENTER_MANAGER')")
    public ResponseEntity<ApiResponse<BatchResponse>> updateBatch(
            @PathVariable Long id,
            @Valid @RequestBody BatchRequest request) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        if (principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CENTER_MANAGER"))) {
            request.setCenterId(getCenterIdForUserAccount(principal));
        }

        BatchResponse batch = batchService.updateBatch(id, request);
        return ResponseEntity.ok(ApiResponse.success("Batch updated successfully", batch));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteBatch(@PathVariable Long id) {
        batchService.deleteBatch(id);
        return ResponseEntity.ok(ApiResponse.success("Batch deleted successfully", "Deleted batch with ID: " + id));
    }

    private Long getCenterIdForUserAccount(UserPrincipal principal) {
        return userAccountRepository.findById(principal.getId())
                .map(user -> user.getCenter() != null ? user.getCenter().getCenterId() : null)
                .orElse(null);
    }
}
