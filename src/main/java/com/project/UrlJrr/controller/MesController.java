package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.ProcessWorker;
import com.project.UrlJrr.entity.ProductLog;
import com.project.UrlJrr.entity.ProductionOrder;
import com.project.UrlJrr.service.ProductLogService;
import com.project.UrlJrr.service.ProductionOrderService;
import com.project.UrlJrr.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mes")
public class MesController {
    private final ProductLogService productLogService;
    private final WorkerService workerService;
    private final ProductionOrderService productionOrderService;

    @GetMapping("/home")
    public String home() {
        return "pages/mes/home";
    }

    @GetMapping("/productLog")
    public String MesHome() {
        return "pages/mes/productLog";
    }

    @GetMapping("/worker")
    public String mesWorker() {
        return "pages/mes/workerRegister";
    }

    @GetMapping("/worker/{id}")
    public String EmployeeDetail(@PathVariable Long id, Model model) {
        ProcessWorker processWorkerDto = workerService.getProcessWorkerById(id);
        model.addAttribute("processWorker", processWorkerDto);
        return "pages/mes/workerDetail";
    }

    @GetMapping("/testSearch")
    public String testSearch() {
        return "pages/mes/testSearch";
    }


    @GetMapping("/popUp")
    public String popUp() {
        return "pages/mes/popUp";
    }

    @GetMapping("/productLog/{id}")
    public String productLogDetail(@PathVariable Long id, Model model) {
        // id를 이용하여 해당 제품 로그 정보를 조회하고 모델에 담아서 전달
        ProductLog productLogDto = productLogService.getProductLogById(id);
        model.addAttribute("productLog", productLogDto);
        return "pages/mes/productModify";
    }

    @GetMapping("/firstProduction")
    public String firstProductionTest() {
        return "pages/mes/firstProduction";
    }

    @GetMapping("/selectProductionOrder")
    public String getProductionOrder(Model model) {
        Map<String, List<String>> processAndEquipment = productionOrderService.findAllProcessAndEquipment();
        model.addAttribute("processAndEquipment", processAndEquipment);

        return "pages/mes/selectProductionOrder";
    }

    @GetMapping("/productionOrder")
    public String getProductionOrder(@RequestParam String processName,
                                     @RequestParam String equipmentName,
                                     Model model) {
        System.out.println("Process Name: " + processName);
        System.out.println("Equipment Name: " + equipmentName);
        List<ProductionOrder> productionOrders = productionOrderService.findOrdersByProcessAndEquipment(processName, equipmentName);
        model.addAttribute("productionOrders", productionOrders);
        return "pages/mes/productionOrder";
    }

    @GetMapping("/productionOrderRegister")
    public String getProductionOrderRegister(
            @RequestParam(name = "processName") String processName,
            @RequestParam(name = "equipmentName") String equipmentName,
            @RequestParam(name = "workerName") String workerName,
            Model model) {
        // 받은 데이터를 모델에 담거나 필요한 처리를 수행합니다.
        // 여기에서는 간단히 로그를 출력해보겠습니다.

        model.addAttribute("processName",processName);
        model.addAttribute("equipmentName",equipmentName);
        model.addAttribute("workerName",workerName);

        // 처리한 후에 해당하는 페이지로 이동합니다.
        return "pages/mes/productionOrderRegister";
    }
}
