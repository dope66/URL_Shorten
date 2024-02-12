package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.ProcessWorkerDto;
import com.project.UrlJrr.entity.ProcessWorker;
import com.project.UrlJrr.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkerService {
    private final WorkerRepository workerRepository;
    public ProcessWorker workerRegister(ProcessWorkerDto processWorkerDto) {
        ProcessWorker processWorker = new ProcessWorker();
        processWorker.setProcessName(processWorkerDto.getProcessName());
        processWorker.setNation(processWorkerDto.getNation());
        processWorker.setPosition(processWorkerDto.getPosition());
        processWorker.setWorkShift(processWorkerDto.getWorkShift());
        processWorker.setWorkerName(processWorkerDto.getWorkerName());
        processWorker.setEquipmentName(processWorkerDto.getEquipmentName());

        return workerRepository.save(processWorker);
    }

    public Page<ProcessWorker> findByWorkerNameContaining(String search, Pageable pageable) {
        return workerRepository.findByWorkerNameContaining(search,pageable);
    }

    public Page<ProcessWorker> findAll(Pageable pageable) {
        return workerRepository.findAll(pageable);
    }

    public long getTotalEmployeeCount() {
        return workerRepository.count();
    }
}
