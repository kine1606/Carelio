package com.Carelio.worker_service.controller;

import com.Carelio.worker_service.dto.request.UpdateWorkerProfileRequest;
import com.Carelio.worker_service.dto.request.UpdateWorkerSkillRequest;
import com.Carelio.worker_service.dto.request.WorkerProfileRequest;
import com.Carelio.worker_service.dto.request.WorkerSkillRequest;
import com.Carelio.worker_service.dto.response.WorkerProfileResponse;
import com.Carelio.worker_service.dto.response.WorkerSkillResponse;
import com.Carelio.worker_service.service.WorkerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workers")
// Bỏ @PreAuthorize ở đây để mở cửa cho CUSTOMER gọi các API xem hồ sơ
public class WorkerController
{

    private final WorkerService workerService;

    // =========================================================================
    // SECTION 1: CÁC API DÀNH RIÊNG CHO THỢ (WORKER ACTIONS)
    // =========================================================================

    @PostMapping
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponse> createWorker(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid WorkerProfileRequest request)
    {
        String userId = jwt.getSubject();
        WorkerProfileResponse response = workerService.createWorkerProfile(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/skills")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerSkillResponse> addWorkerSkill(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid WorkerSkillRequest request)
    {
        String userId = jwt.getSubject();
        WorkerSkillResponse response = workerService.addWorkerSkill(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/orders/{orderId}/accept")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponse> acceptOrder(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId)
    {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(workerService.acceptOrder(userId, orderId));
    }

    @PatchMapping("/orders/{orderId}/start")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponse> startOrder(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId)
    {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(workerService.startOrder(userId, orderId));
    }

    @PatchMapping("/orders/{orderId}/complete")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponse> completeOrder(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId)
    {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(workerService.completeOrder(userId, orderId));
    }

    //========================= Patch and Delete ==================================
    @PatchMapping("/profile")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponse> updateWorkerProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid UpdateWorkerProfileRequest request) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(workerService.updateWorkerProfile(userId, request));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<Void> deleteWorkerProfile(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        workerService.deleteWorkerProfile(userId);
        return ResponseEntity.noContent().build(); // Trả về 204 No Content chuẩn REST
    }

    @PatchMapping("/skills/{skillId}")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerSkillResponse> updateWorkerSkill(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long skillId,
            @RequestBody @Valid UpdateWorkerSkillRequest request) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(workerService.updateWorkerSkill(userId, skillId, request));
    }

    @DeleteMapping("/skills/{skillId}")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<Void> deleteWorkerSkill(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long skillId) {
        String userId = jwt.getSubject();
        workerService.deleteWorkerSkill(userId, skillId);
        return ResponseEntity.noContent().build(); // Trả về 204 No Content
    }
    // =========================================================================
    // SECTION 2: CÁC API XEM THÔNG TIN (CUSTOMER & ADMIN & WORKER ĐỀU VÀO ĐƯỢC)
    // =========================================================================

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<List<WorkerProfileResponse>> getAllWorkers()
    {
        return ResponseEntity.ok(workerService.getAllWorkerProfiles());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<WorkerProfileResponse>> getAllWorkersWithPagination(
            @PageableDefault Pageable pageable
    )
    {
        return ResponseEntity.ok(workerService.getAllWorkerProfilesWithPagination(pageable));
    }
    @GetMapping("/{workerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER') or hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponse> getWorkerById(
            @PathVariable Long workerId)
    {
        return ResponseEntity.ok(workerService.getById(workerId));
    }

    @GetMapping("/{workerId}/profile")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER') or hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponse> getWorkerProfile(
            @PathVariable String userId)
    {
        return ResponseEntity.ok(workerService.getByKeyCloakUserId(userId));
    }

    @GetMapping("/{workerId}/skills")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER') or hasRole('WORKER')")
    public ResponseEntity<List<WorkerSkillResponse>> getWorkerSkills(
            @PathVariable Long workerId)
    {
        return ResponseEntity.ok(workerService.getWorkerSkills(workerId));
    }


}