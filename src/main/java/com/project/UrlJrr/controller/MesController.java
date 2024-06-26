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
    public String home(Model model) {
        Map<String, List<String>> processAndEquipment = productionOrderService.findAllProcessAndEquipment();
        model.addAttribute("processAndEquipment", processAndEquipment);
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
    public String getProductionOrder() {
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

    @GetMapping("/workerProductionOrder")
    public String workerProductionOrder(Model model) {
        Map<String, List<String>> processAndEquipment = productionOrderService.findAllProcessAndEquipment();
        model.addAttribute("processAndEquipment", processAndEquipment);
        return "pages/mes/workerProductionOrder";
    }
    @GetMapping("/orderList")
    public String orderList(){
        return "pages/mes/orderList";
    }
    @GetMapping("/admin")
    public String admin(){
        return "pages/mes/admin";
    }
    @GetMapping("/upload")
    public String upload(){
        return "pages/mes/fileupload";
    }
    @GetMapping("/excel/list")
    public String excelList(){
        return "pages/mes/excelList";
    }

    @GetMapping("/basic/list")
    public String basicDataList(){
        return "pages/mes/basicList";
    }

    @GetMapping("/monitoring")
    public String monitoring(){
        return "pages/mes/monitoring";
    }

}
