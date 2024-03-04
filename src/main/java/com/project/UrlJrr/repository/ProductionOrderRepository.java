package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder,Long> {
    @Query("SELECT distinct p.processName from ProductionOrder p")
    List<String> findAllByProcessName();

    @Query("SELECT distinct p.equipmentName from ProductionOrder p")
    List<String> findAllEquipmentName();
    @Query("SELECT o FROM ProductionOrder o WHERE o.equipmentName = :equipmentName AND o.processName = :processName")
    List<ProductionOrder> findByProcessNameAndEquipmentName(@Param("processName") String processName, @Param("equipmentName") String equipmentName);



}
