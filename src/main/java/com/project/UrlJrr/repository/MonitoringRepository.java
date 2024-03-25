package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.Monitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MonitoringRepository extends JpaRepository<Monitoring, Long> {
    @Query("SELECT m FROM Monitoring m WHERE m.productionDate >= :startOfDay AND m.productionDate < :nextDay")
    List<Monitoring> findAllToday(@Param("startOfDay") Date startOfDay, @Param("nextDay") Date nextDay);



}
