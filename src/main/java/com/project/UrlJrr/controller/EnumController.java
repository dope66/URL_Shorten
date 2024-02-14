package com.project.UrlJrr.controller;

import com.project.UrlJrr.mesenum.EquipmentNameEnum;
import com.project.UrlJrr.mesenum.PositionEnum;
import com.project.UrlJrr.mesenum.ProcessType;
import com.project.UrlJrr.mesenum.WorkShiftEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnumController {

    @GetMapping("/enums/processTypes")
    public ProcessType[] getProcessTypes() {
        return ProcessType.values();
    }

    @GetMapping("/enums/WorkShiftEnum")
    public WorkShiftEnum[] getWorkShiftEnum() {
        return WorkShiftEnum.values();
    }
    @GetMapping("/enums/equipmentName")
    public EquipmentNameEnum[] getEquipmentNameEnum(){
        return EquipmentNameEnum.values();
    }

    @GetMapping("/enums/positionEnum")
    public PositionEnum[] getPositionEnum(){
        return PositionEnum.values();
    }
}