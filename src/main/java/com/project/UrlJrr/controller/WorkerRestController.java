package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.ProcessWorkerDto;
import com.project.UrlJrr.entity.ProcessWorker;
import com.project.UrlJrr.entity.ProductionTest;
import com.project.UrlJrr.service.ProductionTestService;
import com.project.UrlJrr.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/worker")
public class WorkerRestController {
    private final WorkerService workerService;
    private final ProductionTestService productionTestService;

    @Value("${external.directory.path}")
    private String externalDirectoryPath;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registryWorker(@RequestPart(name = "image", required = false) MultipartFile imageFile,
                                            @ModelAttribute ProcessWorkerDto processWorkerDto) {
        String imagePath = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // 외부 디렉토리 경로 수정
                String directoryPath = externalDirectoryPath.replace("file:", ""); // "file:" 접두어 제거
                String workerDirectoryPath = Paths.get(directoryPath, "worker").toString(); // worker 폴더 경로
                File workerDirectory = new File(workerDirectoryPath);
                if (!workerDirectory.exists()) {
                    workerDirectory.mkdirs(); // worker 폴더가 없으면 생성
                }

                String fileName = "image_" + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                imagePath = Paths.get(workerDirectoryPath, fileName).toString();
                imageFile.transferTo(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Failed to save image", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        ProcessWorker newWorker = workerService.workerRegister(processWorkerDto, imagePath); // 서비스 메소드 수정에 따라 imagePath 추가
        return new ResponseEntity<>(newWorker, HttpStatus.CREATED);
    }

    @GetMapping("/getBase64Image")
    public ResponseEntity<?> getBase64Image(@RequestParam Long id) {
        try {
            String imagePath = workerService.getImagePathByWorkerId(id); // DB에서 작업자 ID에 해당하는 이미지 경로 조회

            // 이미지 파일 존재 여부 확인
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                // 파일이 존재하지 않을 경우, 404 상태 코드와 함께 메시지 반환
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
            }

            String base64Image = Base64.getEncoder().encodeToString(Files.readAllBytes(imageFile.toPath()));
            return ResponseEntity.ok("data:image/jpeg;base64," + base64Image);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to convert image to Base64", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/list")
    public ResponseEntity<List<ProcessWorker>> workerList() {
        List<ProcessWorker> workers;
        workers = workerService.findAll();
        return new ResponseEntity<>(workers, HttpStatus.OK);
    }

    @GetMapping("/totalEmployeeCount")
    public ResponseEntity<Long> getTotalEmployeeCount() {
        long totalEmployeeCount = workerService.getTotalEmployeeCount();
        return ResponseEntity.ok(totalEmployeeCount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id) {
        workerService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/modify/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> modifyProcessWorker(@PathVariable("id") Long id,
                                                 @RequestPart(name = "image", required = false) MultipartFile imageFile,
                                                 @ModelAttribute ProcessWorkerDto processWorkerDto) {
        ProcessWorker existingProcessWorker = workerService.getProcessWorkerById(id);
        if (existingProcessWorker == null) {
            return ResponseEntity.notFound().build();
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String directoryPath = externalDirectoryPath.replace("file:", "");
                String workerDirectoryPath = Paths.get(directoryPath, "worker").toString();
                File workerDirectory = new File(workerDirectoryPath);
                if (!workerDirectory.exists() && !workerDirectory.mkdirs()) {
                    throw new IOException("Failed to create directory: " + workerDirectoryPath);
                }

                String fileName = "image_" + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                String imagePath = Paths.get(workerDirectoryPath, fileName).toString();
                imageFile.transferTo(new File(imagePath));


                existingProcessWorker.setImagePath(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Failed to save image", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        existingProcessWorker.setWorkerName(processWorkerDto.getWorkerName());
        existingProcessWorker.setProcessName(processWorkerDto.getProcessName());
        existingProcessWorker.setNation(processWorkerDto.getNation());
        existingProcessWorker.setEquipmentName(processWorkerDto.getEquipmentName());
        existingProcessWorker.setPosition(processWorkerDto.getPosition());
        existingProcessWorker.setWorkShift(processWorkerDto.getWorkShift());

        ProcessWorker updatedProcessWorker = workerService.modifyProcessWorker(existingProcessWorker);
        return ResponseEntity.ok(updatedProcessWorker);
    }


    @GetMapping("/getProcessName")
    public ResponseEntity<?> getProcessName() {
        List<String> processNames = workerService.getProcessName();
        return new ResponseEntity<>(processNames, HttpStatus.OK);
    }

    @GetMapping("/getEquipmentName")
    public ResponseEntity<?> getEquipmentName(@RequestParam(name = "processName") String processName) {
        List<String> equipmentNames = workerService.getEquipmentNamesByProcessName(processName);
        return new ResponseEntity<>(equipmentNames, HttpStatus.OK);
    }

    @GetMapping("/getAllEquipmentName")
    public ResponseEntity<?> getAllEquipmentName() {
        List<String> equipmentNames = workerService.getAllEquipmentNames();
        return new ResponseEntity<>(equipmentNames, HttpStatus.OK);
    }

    @GetMapping("/getAllWorkerName")
    public ResponseEntity<?> getAllWorkerName() {
        List<String> workerNames = workerService.getAllWorkerNames();
        return new ResponseEntity<>(workerNames, HttpStatus.OK);
    }

    @GetMapping("/getWorkerName")
    public ResponseEntity<?> getWorkerName(@RequestParam(name = "processName") String processName,
                                           @RequestParam(name = "equipmentName") String equipmentName) {
        List<String> workerNames = workerService.getWorkerNamesByProcessAndEquipment(processName, equipmentName);
        return new ResponseEntity<>(workerNames, HttpStatus.OK);
    }

    @GetMapping("/getWorkerNameWithProcessName")
    public ResponseEntity<?> getWorkerNameByProcessName(@RequestParam(name = "processName") String processName) {
        List<String> workerNames = workerService.getWorkerNamesByProcessName(processName);
        return new ResponseEntity<>(workerNames, HttpStatus.OK);
    }

    @GetMapping("/getWorkerId")
    public ResponseEntity<?> getWorkerName(@RequestParam(name = "processName") String processName,
                                           @RequestParam(name = "equipmentName") String equipmentName,
                                           @RequestParam(name = "workerName") String workerName) {
        List<String> workerIds = workerService.getIdByProcessNameAndEquipmentNameAndWorkerName(processName, equipmentName, workerName);
        return new ResponseEntity<>(workerIds, HttpStatus.OK);

    }

    @GetMapping("/getWorkerIdWithProcessNameAndWorkerName")
    public ResponseEntity<?> getWorkerIdWithProcessNameAndWorkerName(@RequestParam(name = "processName") String processName,
                                                                     @RequestParam(name = "workerName") String workerName) {
        List<String> workerIds = workerService.getIdByProcessNameAndWorkerName(processName, workerName);
        return new ResponseEntity<>(workerIds, HttpStatus.OK);

    }

    @GetMapping("/testList")
    public ResponseEntity<?> getTestList() {
        List<ProductionTest> productionTests;
        productionTests = productionTestService.findAll();
        return new ResponseEntity<>(productionTests, HttpStatus.OK);
    }
    @GetMapping("/getProcessAndEquipment")
    public ResponseEntity<List<String>> getProcessAndEquipmentNames() {
        List<String> processAndEquipmentNames = workerService.getProcessAndEquipmentNames();
        return ResponseEntity.ok(processAndEquipmentNames);
    }
}