package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.ProcessWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<ProcessWorker, Long> {


    @Query("SELECT DISTINCT pw.workerName FROM ProcessWorker pw")
    List<String> findDistinctWorkerNames();

    @Query("SELECT DISTINCT processName from ProcessWorker ")
    List<String> findByProcessName();
    @Query("SELECT DISTINCT equipmentName FROM ProcessWorker WHERE processName = :processName")
    List<String> findEquipmentNamesByProcessName(@Param("processName")String processName);

    @Query("SELECT DISTINCT worker.workerName FROM ProcessWorker worker WHERE worker.processName = :processName AND worker.equipmentName = :equipmentName")
    List<String> findWorkerNamesByProcessAndEquipment(@Param("processName") String processName, @Param("equipmentName") String equipmentName);
    @Query("SELECT worker.id FROM ProcessWorker worker WHERE worker.processName = :processName AND worker.equipmentName = :equipmentName AND worker.workerName =:workerName")
    List<String> findIdByProcessNameAndEquipmentNameAndWorkerName(@Param("processName") String processName,@Param("equipmentName") String equipmentName, @Param("workerName") String workerName);
    @Query("SELECT DISTINCT equipmentName from ProcessWorker ")
    List<String> findByEquipmentName();
    @Query("SELECT worker.workerName FROM ProcessWorker worker")
    List<String> findByWorkerName();
    @Query("SELECT worker.workerName FROM ProcessWorker worker WHERE worker.processName = :processName")
    List<String> findWorkerNamesByProcessName(@Param("processName") String processName);
}
