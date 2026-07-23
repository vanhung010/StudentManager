package com.vhung.studentmanager.controller;

import com.vhung.studentmanager.dto.response.ApiResponse;
import com.vhung.studentmanager.dto.response.PageResponse;
import com.vhung.studentmanager.dto.response.TeacherResponseDTO;
import com.vhung.studentmanager.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    // Lấy DANH SÁCH giảng viên, có phân trang và có thể lọc theo tên/khoa.
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<TeacherResponseDTO>>> getTeachers(
            @RequestParam(defaultValue = "0") int page,             // Trang thứ mấy, mặc định trang 0
            @RequestParam(defaultValue = "20") int size,            // Mỗi trang bao nhiêu giảng viên, mặc định 20
            @RequestParam(required = false) String name,            // Tên để tìm
            @RequestParam(required = false) Long departmentId       // Lọc theo khoa
    ) {
        // Gộp page/size/sort lại thành 1 "gói yêu cầu phân trang" gửi xuống cho database.
        // Sort.by("fullName").ascending(): sắp xếp kết quả theo tên, từ A -> Z.
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName").ascending());

        // Nhờ Service xử lý hết phần "NẶNG" (lọc, phân trang, đổi Entity -> DTO)
        PageResponse<TeacherResponseDTO> result = teacherService.getTeachers(name, departmentId, pageable);

        // Gói kết quả vào ApiResponse trả về OK:)))
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> getTeacherById(@PathVariable Long id) {
        TeacherResponseDTO response = teacherService.getTeacherById(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}