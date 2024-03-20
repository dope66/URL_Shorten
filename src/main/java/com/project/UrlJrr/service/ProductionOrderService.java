package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.ProductOrderDto;
import com.project.UrlJrr.entity.ProductionOrder;
import com.project.UrlJrr.repository.ProductionOrderRepository;
import com.project.UrlJrr.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductionOrderService {
    private final ProductionOrderRepository productionOrderRepository;
    private final WorkerRepository workerRepository;
    public List<String> findAllProcessNames() {
        return productionOrderRepository.findAllByProcessName();

    }

    public List<String> findAllEquipmentNames() {
        return productionOrderRepository.findAllEquipmentName();
    }

    public List<ProductionOrder> findOrdersByProcessAndEquipment(String processName, String equipmentName) {
        return productionOrderRepository.findByProcessNameAndEquipmentName(processName, equipmentName);
    }

    public Map<String, List<String>> findAllProcessAndEquipment() {
        Map<String, List<String>> processEquipmentMap = new HashMap<>();
        List<String> processNames = workerRepository.findByProcessName();

        for (String processName : processNames) {
            List<String> equipmentNames = workerRepository.findEquipmentNamesByProcessName(processName);
            processEquipmentMap.put(processName, equipmentNames);
        }
        return processEquipmentMap;
    }

    public ProductionOrder register(ProductOrderDto productOrderDto) {
        return productionOrderRepository.save(productOrderDto.toEntity());
    }

    public List<ProductionOrder> findAll() {
        return productionOrderRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<ProductionOrder> findAllProducedToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // 자정으로 시간 설정
        cal.set(Calendar.MINUTE, 0); // 시간당 분 설정
        cal.set(Calendar.SECOND, 0); // 분당 초 설정
        cal.set(Calendar.MILLISECOND, 0); // 초당 밀리초 설정

        Date startOfDay = cal.getTime(); // 오늘의 시작

        cal.add(Calendar.DAY_OF_MONTH, 1); // 다음 날의 시작을 위해 1일 추가
        Date nextDay = cal.getTime(); // 다음 날의 시작

        return productionOrderRepository.findAllProducedToday(startOfDay, nextDay);
    }
}
