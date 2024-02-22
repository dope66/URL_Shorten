package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.ProcessWorkerDto;
import com.project.UrlJrr.entity.ProcessWorker;
import com.project.UrlJrr.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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


//    public Page<ProcessWorker> findByWorkerNameContaining(String workerName, Pageable pageable) {
//        return workerRepository.findByWorkerNameContaining(workerName, pageable);
//    }

    //        public Page<ProcessWorker> findAll(Pageable pageable) {
//        return workerRepository.findAll(pageable);
//    }
    public List<ProcessWorker> findAll() {
        return workerRepository.findAll();
    }

    //    public List<ProcessWorker> findByWorkerNameContaining(String search) {
//        return workerRepository.findByWorkerNameContaining(search);
//    }
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

    public String getImagePathByWorkerId(Long id) {
        ProcessWorker ImagePathByprocessWorker = getProcessWorkerById(id);
        System.out.println("processWorkerImagePath " + ImagePathByprocessWorker);
        return ImagePathByprocessWorker.getImagePath();
    }


    public List<String> getEquipmentNamesByProcessName(String processName) {
        return workerRepository.findEquipmentNamesByProcessName(processName);
    }

    public List<String> getWorkerNamesByProcessAndEquipment(String processName, String equipmentName) {
        return workerRepository.findWorkerNamesByProcessAndEquipment(processName, equipmentName);
    }


    public List<String> getProcessName() {
        return workerRepository.findByProcessName();
    }

    public List<String> getIdByProcessNameAndEquipmentNameAndWorkerName(String processName, String equipmentName, String workerName) {
        return workerRepository.findIdByProcessNameAndEquipmentNameAndWorkerName(processName, equipmentName, workerName);
    }
}
