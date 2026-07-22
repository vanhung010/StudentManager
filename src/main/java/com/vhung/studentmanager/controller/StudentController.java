package com.vhung.studentmanager.controller;

import com.vhung.studentmanager.dto.request.StudentRequestDTO;
import com.vhung.studentmanager.dto.response.ApiResponse;
import com.vhung.studentmanager.dto.response.StudentResponseDTO;
import com.vhung.studentmanager.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDTO>> create(@RequestBody StudentRequestDTO request){
        StudentResponseDTO studentResponseDTO = studentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(studentResponseDTO));
    }
}
