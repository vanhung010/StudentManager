package com.vhung.studentmanager.controller;

import com.vhung.studentmanager.dto.request.DepartmentRequestDTO;
import com.vhung.studentmanager.dto.response.ApiResponse;
import com.vhung.studentmanager.dto.response.DepartmentResponseDTO;
import com.vhung.studentmanager.dto.response.PageResponse;
import com.vhung.studentmanager.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    //api/departments
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DepartmentResponseDTO>>> getAll(
            @RequestParam(defaultValue = "active") String status,// active | deleted | all
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        PageResponse<DepartmentResponseDTO> data = departmentService.getAllDepartment(status, keyword, pageable);
        return  ResponseEntity.ok(ApiResponse.ok(data));
    }

    //api/departments
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> create(@RequestBody DepartmentRequestDTO departmentRequestDTO){
        DepartmentResponseDTO data = departmentService.create(departmentRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(data));
    }

    //PUT api/departments/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> update(@PathVariable Long id, @RequestBody @Validated DepartmentRequestDTO departmentRequestDTO) {
        DepartmentResponseDTO data = departmentService.update(id, departmentRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> restore(@PathVariable Long id){
        DepartmentResponseDTO data = departmentService.restore(id);

        return ResponseEntity.ok(ApiResponse.ok(data));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> get(@PathVariable Long id){
        DepartmentResponseDTO data = departmentService.get(id);

        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleted(@PathVariable Long id){
        departmentService.deleted(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }


}
