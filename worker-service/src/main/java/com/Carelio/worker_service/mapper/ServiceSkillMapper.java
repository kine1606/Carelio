package com.Carelio.worker_service.mapper;

import com.Carelio.worker_service.dto.request.ServiceSkillRequest;
import com.Carelio.worker_service.dto.response.ServiceSkillResponse;
import com.Carelio.worker_service.entity.ServiceSkill;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceSkillMapper
{
    ServiceSkillResponse toResponse(ServiceSkill serviceSkill);
    ServiceSkill toEntity(ServiceSkillRequest request);

    List<ServiceSkillResponse> toResponseList(List<ServiceSkill> list);
}
