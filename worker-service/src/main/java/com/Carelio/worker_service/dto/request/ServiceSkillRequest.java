package com.Carelio.worker_service.dto.request;

import com.Carelio.worker_service.entity.ServiceSkillCode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceSkillRequest
{
    private ServiceSkillCode serviceSkillCode;
}
