package com.Carelio.worker_service.dto.response;

import com.Carelio.worker_service.entity.ServiceSkillCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceSkillResponse
{
    private Long id;
    private ServiceSkillCode serviceSkillCode;
}
