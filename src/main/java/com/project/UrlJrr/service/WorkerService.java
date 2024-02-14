package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.ProcessWorkerDto;
import com.project.UrlJrr.entity.ProcessWorker;
import com.project.UrlJrr.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkerService {
    private final WorkerRepository workerRepository;
    public ProcessWorker workerRegister(ProcessWorkerDto processWorkerDto, String imagePath) {
        ProcessWorker processWorker = new ProcessWorker();
        processWorker.setProcessName(processWorkerDto.getProcessName());
        processWorker.setNation(processWorkerDto.getNation());
        processWorker.setPosition(processWorkerDto.getPosition());
        processWorker.setWorkShift(processWorkerDto.getWorkShift());
        processWorker.setWorkerName(processWorkerDto.getWorkerName());
        processWorker.setEquipmentName(processWorkerDto.getEquipmentName());
        processWorker.setImagePath(imagePath); // 이미지 경로 설정

        return workerRepository.save(processWorker);
    }


    public Page<ProcessWorker> findByWorkerNameContaining(String workerName, Pageable pageable) {
        return workerRepository.findByWorkerNameContaining(workerName,pageable);
    }

    public Page<ProcessWorker> findAll(Pageable pageable) {
        return workerRepository.findAll(pageable);
    }

    public long getTotalEmployeeCount() {
        return workerRepository.count();
    }

    public void deleteEmployee(Long id) {
        workerRepository.deleteById(id);
    }

    public ProcessWorker getProcessWorkerById(Long id) {
        Optional<ProcessWorker> optionalProcessWorker = workerRepository.findById(id);
        return optionalProcessWorker.orElse(null);
    }

    public ProcessWorker modifyProcessWorker(ProcessWorker processWorker) {
        return workerRepository.save(processWorker);
    }
}
