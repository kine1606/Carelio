package com.Carelio.worker_service.base;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BaseResponse {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}