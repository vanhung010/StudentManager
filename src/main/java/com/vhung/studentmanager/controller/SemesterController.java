package com.vhung.studentmanager.controller;

import com.vhung.studentmanager.dto.request.SemesterRequestDTO;
import com.vhung.studentmanager.dto.response.ApiResponse;
import com.vhung.studentmanager.dto.response.SemesterResponseDTO;
import com.vhung.studentmanager.service.SemesterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/semesters")
@RequiredArgsConstructor
public class SemesterController {
    private final SemesterService semesterService;
    @PostMapping
    public ResponseEntity<ApiResponse<SemesterResponseDTO>> create(@RequestBody @Valid SemesterRequestDTO semesterRequestDTO){
        SemesterResponseDTO data = semesterService.create(semesterRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok("Tạo học kì mới thành công", data));
    }
}
