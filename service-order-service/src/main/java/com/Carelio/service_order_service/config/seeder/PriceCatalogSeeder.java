package com.Carelio.service_order_service.config.seeder;

import com.Carelio.service_order_service.entity.PriceCatalog;
import com.Carelio.service_order_service.repository.PriceCatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PriceCatalogSeeder implements CommandLineRunner {

    private final PriceCatalogRepository priceCatalogRepository;

    @Override
    public void run(String... args) throws Exception {
        // Kiểm tra xem nếu bảng chưa có cấu hình giá nào thì mới thực hiện seed
        if (priceCatalogRepository.count() == 0) {
            log.info("Bắt đầu khởi tạo dữ liệu mẫu cho Ma trận giá (Price Catalog)...");

            PriceCatalog quatDienVeSinh = PriceCatalog.builder()
                    .equipmentCategoryId(1L) // 1: Quạt điện
                    .serviceSkillId(1L)      // 1: Vệ sinh
                    .price(new BigDecimal("100000.00")) // 100k
                    .build();

            PriceCatalog mayLanhVeSinh = PriceCatalog.builder()
                    .equipmentCategoryId(2L) // 2: Máy lạnh
                    .serviceSkillId(1L)      // 1: Vệ sinh
                    .price(new BigDecimal("250000.00")) // 250k
                    .build();

            PriceCatalog mayLanhSuaBo = PriceCatalog.builder()
                    .equipmentCategoryId(2L) // 2: Máy lạnh
                    .serviceSkillId(2L)      // 2: Sửa bo mạch
                    .price(new BigDecimal("600000.00")) // 600k
                    .build();

            // Lưu tất cả vào Database
            priceCatalogRepository.saveAll(List.of(quatDienVeSinh, mayLanhVeSinh, mayLanhSuaBo));

            log.info("Seed dữ liệu Ma trận giá thành công! Đã nạp 3 bản ghi mẫu.");
        } else {
            log.info("Bảng Ma trận giá đã có dữ liệu, bỏ qua bước seed.");
        }
    }
}