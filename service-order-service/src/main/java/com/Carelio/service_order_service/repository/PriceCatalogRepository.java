package com.Carelio.service_order_service.repository;

import com.Carelio.service_order_service.entity.PriceCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PriceCatalogRepository extends JpaRepository<PriceCatalog, Long> {

    // Tìm cấu hình giá dựa trên sự kết hợp của Loại thiết bị và Loại dịch vụ
    Optional<PriceCatalog> findByEquipmentCategoryIdAndServiceSkillId(Long equipmentCategoryId, Long serviceSkillId);
}