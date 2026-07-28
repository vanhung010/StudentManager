package com.vhung.studentmanager.controller;

import com.vhung.studentmanager.dto.request.ClassRequestDTO;
import com.vhung.studentmanager.dto.response.ApiResponse;
import com.vhung.studentmanager.dto.response.ClassResponseDTO;
import com.vhung.studentmanager.service.ClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClassResponseDTO>> create(@RequestBody @Valid ClassRequestDTO request){
        ClassResponseDTO data = classService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(data));
    }

}
