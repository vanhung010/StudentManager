package com.vhung.studentmanager.controller;

import com.vhung.studentmanager.dto.request.ClassRequestDTO;
import com.vhung.studentmanager.dto.response.ApiResponse;
import com.vhung.studentmanager.dto.response.ClassResponseDTO;
import com.vhung.studentmanager.dto.response.PageResponse;
import com.vhung.studentmanager.service.ClassService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClassResponseDTO>> create(@Valid @RequestBody ClassRequestDTO request){
        ClassResponseDTO data = classService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ClassResponseDTO>>> getAll(
            @RequestParam(required = false)  String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer enrollmentYear,
            @RequestParam(required = false)  Long idDepartment,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size){
        Pageable pageable = PageRequest.of(page, size);

        PageResponse<ClassResponseDTO> data = classService.getAll(keyword, enrollmentYear, status, idDepartment, pageable);

        return ResponseEntity.ok(ApiResponse.ok(data));

    }


    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassResponseDTO>> restore(@PathVariable Long id){
        ClassResponseDTO data = classService.restore(id);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleted(@PathVariable Long id){
        classService.deleted(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
