package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.ProductOrderDto;
import com.project.UrlJrr.entity.ProductionOrder;
import com.project.UrlJrr.repository.ProductionOrderRepository;
import com.project.UrlJrr.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
