package com.vhung.studentmanager.controller;

import com.vhung.studentmanager.dto.request.StudentRequestDTO;
import com.vhung.studentmanager.dto.request.UpdateStudentRequest;
import com.vhung.studentmanager.dto.response.ApiResponse;
import com.vhung.studentmanager.dto.response.StudentResponseDTO;
import com.vhung.studentmanager.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDTO>> create(@RequestBody @Valid StudentRequestDTO request){
        StudentResponseDTO studentResponseDTO = studentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(studentResponseDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> update(@PathVariable Long id, @RequestBody UpdateStudentRequest request){
        StudentResponseDTO studentResponseDTO = studentService.update(id, request);
        return ResponseEntity.status(200).body(ApiResponse.ok(studentResponseDTO));
    }
}
