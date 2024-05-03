package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.ProductionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionItemRepository extends JpaRepository<ProductionItem, Long> {
    // 연관된 ManagementItem과 함께 ProductionItem 조회
    @Query("SELECT p FROM ProductionItem p LEFT JOIN FETCH p.managementItems WHERE p.id = :id")
    ProductionItem findByIdWithManagementItems(@Param("id") Long id);

    // 모든 ProductionItem과 연관된 ManagementItem 리스트 함께 조회
    @Query("SELECT p FROM ProductionItem p LEFT JOIN FETCH p.managementItems")
    List<ProductionItem> findAllWithManagementItems();

}
