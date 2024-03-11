package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.BasicDataDTO;
import com.project.UrlJrr.dto.ManagementItemDTO;
import com.project.UrlJrr.dto.ProductionItemDTO;
import com.project.UrlJrr.entity.BasicData;
import com.project.UrlJrr.entity.InspectionItem;
import com.project.UrlJrr.entity.ManagementItem;
import com.project.UrlJrr.entity.ProductionItem;
import com.project.UrlJrr.repository.BasicDataRepository;
import com.project.UrlJrr.repository.ProductionItemRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelService {
    private static final Logger log = LoggerFactory.getLogger(ExcelService.class);
    private final ProductionItemRepository productionItemRepository;
    private final BasicDataRepository basicDataRepository;

    // 파일을 처리하고 성공 여부를 반환하는 메소드
    public boolean processExcelFile(MultipartFile file) {
        boolean isProcessed = processType1ExcelFile(file);
        // 파일 처리 후 추가적인 로직을 여기에 구현할 수 있습니다.
        return isProcessed;
    }
//    public boolean uploadExcelFile(MultipartFile file) {
//        List<CompletableFuture<ProductionItem>> futures = new ArrayList<>();
//        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
//            XSSFSheet sheet = workbook.getSheetAt(0);
//
//            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
//                Row row = sheet.getRow(rowIndex);
//                if (row == null) continue; // 빈 행은 건너뜁니다.
//
//                CompletableFuture<ProductionItem> future = processRowAsync(row);
//                futures.add(future);
//            }
//
//            // 모든 비동기 작업이 완료될 때까지 기다립니다.
//            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
//
//            return true; // 파일이 성공적으로 처리되었습니다.
//        } catch (IOException e) {
//            log.error("엑셀 파일 처리 중 오류 발생", e);
//            return false; // 파일 처리에 실패했습니다.
//        }
//    }


    //    public ProductionItem getProductionItemWithManagementItems(Long id) {
//        return productionItemRepository.findByIdWithManagementItems(id);
//    }
    private static final int MANAGEMENT_ITEM_START_INDEX = 10; // 셀 인덱스는 0부터 시작합니다.
    private static final int MANAGEMENT_ITEM_CELL_COUNT = 8; // 관리 항목 세트당 셀 수

    private ProductionItem mapRowToProductionItem(Row row) {
        ProductionItem item = new ProductionItem();
        item.setNumber(getCellStringValue(row, 0));
        item.setProductionLine(getCellStringValue(row, 1));
        item.setVehicleType(getCellStringValue(row, 2));
        item.setPartNumber(getCellStringValue(row, 3));
        item.setPartName(getCellStringValue(row, 4));
        item.setProductionFacility(getCellStringValue(row, 5));
        item.setProcessName(getCellStringValue(row, 6));
        item.setProcessNumber(getCellStringValue(row, 7));
        item.setEquipmentName(getCellStringValue(row, 8));
        item.setEquipmentNumber(getCellStringValue(row, 9));
        // 관리 항목 반복 처리 로직 수정
        for (int cellIndex = MANAGEMENT_ITEM_START_INDEX; cellIndex < row.getLastCellNum(); cellIndex += MANAGEMENT_ITEM_CELL_COUNT) {
            if (cellIndex + MANAGEMENT_ITEM_CELL_COUNT - 1 >= row.getLastCellNum()) {
                // 현재 인덱스에서 관리 항목 세트를 완성할 수 없으면 반복 중단
                break;
            }
            ManagementItem managementItem = mapCellsToManagementItem(row, cellIndex);
            item.getManagementItems().add(managementItem);
            managementItem.setProductionItem(item);
        }

        return item;
    }

    @Async
    public CompletableFuture<ProductionItem> processRowAsync(Row row) {
        try {
            ProductionItem item = mapRowToProductionItem(row);
            productionItemRepository.save(item); // 저장 로직이 비동기로 실행됩니다.
            return CompletableFuture.completedFuture(item);
        } catch (Exception e) {
            log.error("행 처리 중 오류 발생", e);
            // 실패한 경우, null을 반환할 수 있으나, 실제로는 실패 처리 로직을 구현할 필요가 있습니다.
            return CompletableFuture.completedFuture(null);
        }
    }

    private ManagementItem mapCellsToManagementItem(Row row, int startIndex) {
        ManagementItem managementItem = new ManagementItem();
        managementItem.setManagementNumber(getCellStringValue(row, startIndex));
        managementItem.setManagementItem(getCellStringValue(row, startIndex + 1));
        managementItem.setSpecialCharacteristic(getCellStringValue(row, startIndex + 2));
        managementItem.setStandard(getCellStringValue(row, startIndex + 3));
        managementItem.setVerificationMethod(getCellStringValue(row, startIndex + 4));
        managementItem.setFrequency(getCellStringValue(row, startIndex + 5));
        managementItem.setRecord(getCellStringValue(row, startIndex + 6));
        managementItem.setActionForAbnormality(getCellStringValue(row, startIndex + 7));

        return managementItem;
    }

    // 셀 값을 안전하게 읽어오는 헬퍼 메소드
    private String getCellStringValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // 숫자 데이터를 문자열로 변환
                return Double.toString(cell.getNumericCellValue());
            case BOOLEAN:
                // 불리언 데이터를 문자열로 변환
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                // 수식이 있는 경우, 계산된 값을 문자열로 변환
                // 이 예제에서는 간단히 수식의 결과 타입을 기반으로 처리
                // 보다 복잡한 처리가 필요할 경우, FormulaEvaluator 사용 고려
                switch (cell.getCachedFormulaResultType()) {
                    case STRING:
                        return cell.getRichStringCellValue().getString();
                    case NUMERIC:
                        return Double.toString(cell.getNumericCellValue());
                    default:
                        return "";
                }
            default:
                // 다른 타입의 데이터는 필요에 따라 처리
                return "";
        }
    }

    public List<ProductionItemDTO> getAllProductionItemsWithManagementItemsDto() {
        List<ProductionItem> productionItems = productionItemRepository.findAllWithManagementItems();
        return productionItems.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProductionItemDTO convertToDto(ProductionItem productionItem) {
        ProductionItemDTO dto = new ProductionItemDTO();
        dto.setNumber(productionItem.getNumber());
        dto.setProductionLine(productionItem.getProductionLine());
        dto.setVehicleType(productionItem.getVehicleType());
        dto.setPartName(productionItem.getPartName());
        dto.setPartNumber(productionItem.getPartNumber());
        dto.setEquipmentNumber(productionItem.getEquipmentNumber());
        dto.setEquipmentName(productionItem.getEquipmentName());
        dto.setProcessName(productionItem.getProcessName());
        dto.setProcessNumber(productionItem.getProcessNumber());
        dto.setProductionFacility(productionItem.getProductionFacility());
        List<ManagementItemDTO> managementItemDtos = productionItem.getManagementItems().stream()
                .map(this::convertToManagementItemDto)
                .collect(Collectors.toList());
        dto.setManagementItems(managementItemDtos);
        return dto;
    }

    private ManagementItemDTO convertToManagementItemDto(ManagementItem managementItem) {
        ManagementItemDTO dto = new ManagementItemDTO();
        dto.setManagementNumber(managementItem.getManagementNumber());
        dto.setManagementItem(managementItem.getManagementItem());
        dto.setRecord(managementItem.getRecord());
        dto.setFrequency(managementItem.getFrequency());
        dto.setStandard(managementItem.getStandard());
        dto.setSpecialCharacteristic(managementItem.getSpecialCharacteristic());
        dto.setActionForAbnormality(managementItem.getActionForAbnormality());
        dto.setSpecialCharacteristic(managementItem.getSpecialCharacteristic());
        // 나머지 필드도 설정
        return dto;
    }

    public BasicDataDTO convertToDTO(BasicData basicData) {
        BasicDataDTO dto = new BasicDataDTO();
        dto.setNumber(basicData.getNumber());
        dto.setPartName(basicData.getPartName());
        dto.setPartNumber(basicData.getPartNumber());
        dto.setVehicleType(basicData.getVehicleType());

        List<InspectionItem> inspectionItems = basicData.getInspectionItems();
        if (inspectionItems.size() > 0) {
            dto.setInspectionItem1(inspectionItems.get(0).getItem());
            dto.setStandard1(inspectionItems.get(0).getStandard());
            dto.setTolerance1(inspectionItems.get(0).getTolerance());
            dto.setMethod1(inspectionItems.get(0).getMethod());
            dto.setSpecification1(inspectionItems.get(0).getSpecification());
        }
        if (inspectionItems.size() > 1) {
            dto.setInspectionItem2(inspectionItems.get(1).getItem());
            dto.setStandard2(inspectionItems.get(1).getStandard());
            dto.setTolerance2(inspectionItems.get(1).getTolerance());
            dto.setMethod2(inspectionItems.get(1).getMethod());
            dto.setSpecification2(inspectionItems.get(1).getSpecification());
        }
        if (inspectionItems.size() > 2) {
            dto.setInspectionItem3(inspectionItems.get(2).getItem());
            dto.setStandard3(inspectionItems.get(2).getStandard());
            dto.setTolerance3(inspectionItems.get(2).getTolerance());
            dto.setMethod3(inspectionItems.get(2).getMethod());
            dto.setSpecification3(inspectionItems.get(2).getSpecification());
        }
        return dto;
    }

    public boolean processType1ExcelFile(MultipartFile file) {
        List<CompletableFuture<ProductionItem>> futures = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue; // 빈 행은 건너뜁니다.

                CompletableFuture<ProductionItem> future = processRowAsync(row);
                futures.add(future);
            }

            // 모든 비동기 작업이 완료될 때까지 기다립니다.
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            return true; // 파일이 성공적으로 처리되었습니다.
        } catch (IOException e) {
            log.error("엑셀 파일 처리 중 오류 발생", e);
            return false; // 파일 처리에 실패했습니다.
        }
    }

    public boolean processType2ExcelFile(MultipartFile file) {
        List<CompletableFuture<BasicData>> futures = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0); // Type2 데이터를 포함하는 시트

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue; // 빈 행은 건너뜁니다.

                CompletableFuture<BasicData> future = CompletableFuture.supplyAsync(() -> {
                    BasicData basicData = mapRowToBasicData(row);
                    basicDataRepository.save(basicData); // 데이터베이스에 저장
                    return basicData;
                });
                futures.add(future);
            }

            // 모든 비동기 작업이 완료될 때까지 기다립니다.
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            return true; // 파일이 성공적으로 처리되었습니다.
        } catch (IOException e) {
            log.error("엑셀 파일 처리 중 오류 발생", e);
            return false; // 파일 처리에 실패했습니다.
        }
    }

    private BasicData mapRowToBasicData(Row row) {
        // 엔티티 및 관련 객체 생성
        BasicData basicData = new BasicData();
        List<InspectionItem> inspectionItems = new ArrayList<>();
        basicData.setNumber(getCellStringValue(row, 0)); // '순'
        // 필수 정보 매핑
        basicData.setVehicleType(getCellStringValue(row, 1)); // 차종
        basicData.setPartNumber(getCellStringValue(row, 2)); // 품번
        basicData.setPartName(getCellStringValue(row,
                3)); // 품명

        // 검사항목 매핑
        for (int i = 0; i < 3; i++) { // 각 검사항목에 대한 반복 처리
            int baseIndex = 4 + i * 5; // 각 검사항목의 기본 인덱스

            String item = getCellStringValue(row, baseIndex);
            String standard = getCellStringValue(row, baseIndex + 1);
            String method = getCellStringValue(row, baseIndex + 2);
            String specification = getCellStringValue(row, baseIndex + 3);
            String tolerance = getCellStringValue(row, baseIndex + 4);

            // 데이터 검증 및 매핑
            if (item != null && !item.isEmpty()) { // 검사항목이 있는 경우에만 추가
                InspectionItem inspectionItem = new InspectionItem();
                inspectionItem.setItem(item);
                inspectionItem.setStandard(standard);
                inspectionItem.setMethod(method);
                inspectionItem.setSpecification(specification);
                inspectionItem.setTolerance(tolerance);

                inspectionItems.add(inspectionItem); // 리스트에 추가
            }
        }

        basicData.setInspectionItems(inspectionItems); // 완성된 리스트를 기본 데이터에 설정

        return basicData; // 매핑된 엔티티 반환
    }

    public List<BasicDataDTO> findBasicDataAll() {
        List<BasicData> basicDataList = basicDataRepository.findAll();
        return basicDataList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}
