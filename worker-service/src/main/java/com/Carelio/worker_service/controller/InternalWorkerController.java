package com.Carelio.worker_service.controller;

import com.Carelio.worker_service.dto.response.WorkerProfileResponse;
import com.Carelio.worker_service.entity.ServiceSkillCode;
import com.Carelio.worker_service.service.InternalWorkerService;
import com.Carelio.worker_service.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/workers")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('WORKER') or hasRole('ADMIN')")
public class InternalWorkerController {

    private final WorkerService workerService;
    private final InternalWorkerService internalWorkerService;

    // GET /internal/workers/{workerId}
    @GetMapping("/{workerId}")
    public ResponseEntity<WorkerProfileResponse> getInternalWorkerById(@PathVariable Long workerId)
    {
        return ResponseEntity.ok(workerService.getById(workerId));
    }

    // GET /internal/workers/{workerId}/can-handle?serviceType=REPAIR&categoryId=1
    @GetMapping("/{workerId}/can-handle")
    public ResponseEntity<Boolean> canHandle(
            @PathVariable Long workerId,
            @RequestParam ServiceSkillCode serviceSkillCode,
            @RequestParam Long categoryId
    ) {
        boolean result = internalWorkerService.canHandle(workerId, serviceSkillCode, categoryId);
        return ResponseEntity.ok(result);
    }
}
