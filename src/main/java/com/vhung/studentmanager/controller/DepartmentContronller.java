package com.vhung.studentmanager.controller;

import com.vhung.studentmanager.dto.request.DepartmentRequestDTO;
import com.vhung.studentmanager.dto.response.ApiResponse;
import com.vhung.studentmanager.dto.response.DepartmentResponseDTO;
import com.vhung.studentmanager.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentContronller {
    private final DepartmentService departmentService;

    //api/departments
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getAll(){
        List<DepartmentResponseDTO> data = departmentService.getAllDepartment();
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    //api/departments
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> create(@RequestBody @Validated DepartmentRequestDTO departmentRequestDTO){
        DepartmentResponseDTO data = departmentService.create(departmentRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(data));
    }

    //PUT api/departments/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> update(@PathVariable Long id, @RequestBody @Validated DepartmentRequestDTO departmentRequestDTO) {
        DepartmentResponseDTO data = departmentService.update(id, departmentRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    //deleted api/departments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleted(@PathVariable Long id){
        departmentService.deleted(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
