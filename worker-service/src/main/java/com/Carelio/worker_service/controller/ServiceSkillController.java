package com.Carelio.worker_service.controller;

import com.Carelio.worker_service.dto.request.ServiceSkillRequest;
import com.Carelio.worker_service.dto.response.ServiceSkillResponse;
import com.Carelio.worker_service.service.ServiceSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-skills")
public class ServiceSkillController {

    private final ServiceSkillService serviceSkillService;

    // POST /api/service-skills
    @PostMapping
    public ResponseEntity<ServiceSkillResponse> create(@RequestBody @Valid ServiceSkillRequest request) {
        ServiceSkillResponse response = serviceSkillService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/service-skills
    @GetMapping
    public ResponseEntity<List<ServiceSkillResponse>> getAll() {
        return ResponseEntity.ok(serviceSkillService.getAll());
    }
}
