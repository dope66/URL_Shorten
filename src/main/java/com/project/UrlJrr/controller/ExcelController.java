package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.BasicDataDTO;
import com.project.UrlJrr.dto.ProductionItemDTO;
import com.project.UrlJrr.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) {

        // 파일 확장자 검사
        String fileName = file.getOriginalFilename();
        if (!isExcelFile(fileName)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type. Only Excel files are allowed.");
        }

        boolean isProcessed;
        switch (type) {
            case "type1":
                isProcessed = excelService.processType1ExcelFile(file);
                break;
            case "type2":
                isProcessed = excelService.processType2ExcelFile(file);
                break;
            case "type3":
                isProcessed=excelService.processType3ExcelFile(file);
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid type parameter");
        }

        return isProcessed
                ? ResponseEntity.ok("File processed successfully.")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the file.");
    }


    @GetMapping("/list")
    public ResponseEntity<List<ProductionItemDTO>> listDto() {
        List<ProductionItemDTO> dtoList = excelService.getAllProductionItemsWithManagementItemsDto();
        return ResponseEntity.ok(dtoList);
    }

    private boolean isExcelFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".xlsx") || lowerCaseFileName.endsWith(".xls");
    }



    @GetMapping("/basic/list")
    public ResponseEntity<List<BasicDataDTO>> basicListDto(){
        List<BasicDataDTO> dtoList = excelService.findBasicDataAll();
        return ResponseEntity.ok(dtoList);
    }
}
