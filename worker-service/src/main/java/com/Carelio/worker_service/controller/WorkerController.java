package com.Carelio.worker_service.controller;

import com.Carelio.worker_service.dto.request.WorkerProfileRequest;
import com.Carelio.worker_service.dto.request.WorkerSkillRequest;
import com.Carelio.worker_service.dto.response.WorkerProfileResponse;
import com.Carelio.worker_service.dto.response.WorkerSkillResponse;
import com.Carelio.worker_service.service.WorkerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workers")
public class WorkerController {

    private final WorkerService workerService;

    // POST /api/workers
    @PostMapping
    public ResponseEntity<WorkerProfileResponse> createWorker(@RequestBody @Valid WorkerProfileRequest request) {
        WorkerProfileResponse response = workerService.createWorkerProfile(request.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/workers
    @GetMapping
    public ResponseEntity<List<WorkerProfileResponse>> getAllWorkers() {
        return ResponseEntity.ok(workerService.getAllWorkerProfiles());
    }

    // GET /api/workers/{workerId}
    @GetMapping("/{workerId}")
    public ResponseEntity<WorkerProfileResponse> getWorkerById(@PathVariable Long workerId) {
        return ResponseEntity.ok(workerService.getById(workerId));
    }

    // POST /api/workers/{workerId}/skills
    @PostMapping("/{workerId}/skills")
    public ResponseEntity<WorkerSkillResponse> addWorkerSkill(
            @PathVariable Long workerId,
            @RequestBody @Valid WorkerSkillRequest request
    ) {
        WorkerSkillResponse response = workerService.addWorkerSkill(workerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/workers/{workerId}/skills
    @GetMapping("/{workerId}/skills")
    public ResponseEntity<List<WorkerSkillResponse>> getWorkerSkills(@PathVariable Long workerId) {
        return ResponseEntity.ok(workerService.getWorkerSkill(workerId));
    }
}
