package com.Carelio.worker_service.dto.response;

import com.Carelio.worker_service.entity.ServiceSkillCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ServiceSkillResponse
{
    private Long id;
    private ServiceSkillCode serviceSkillCode;
}
