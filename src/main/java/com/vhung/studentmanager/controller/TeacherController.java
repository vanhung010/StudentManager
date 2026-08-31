package com.vhung.studentmanager.controller;

import com.vhung.studentmanager.dto.request.TeacherRequestDTO;
import com.vhung.studentmanager.dto.response.ApiResponse;
import com.vhung.studentmanager.dto.response.PageResponse;
import com.vhung.studentmanager.dto.response.TeacherResponseDTO;
import com.vhung.studentmanager.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TeacherResponseDTO>>> getTeachers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "active") String status
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName").ascending());
        PageResponse<TeacherResponseDTO> result =
                teacherService.getTeachers(name, departmentId, status, pageable);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> getTeacherById(@PathVariable Long id, Authentication authentication) {
        TeacherResponseDTO response = teacherService.getTeacherById(id, authentication);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> getCurrentTeacher(Authentication authentication) {
        String username = authentication.getName();
        TeacherResponseDTO response = teacherService.getCurrentTeacherByUserName(username);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> create(@RequestBody TeacherRequestDTO request){
        TeacherResponseDTO data = teacherService.create(request);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }
    // THÊM: cập nhật thông tin giảng viên + username + optional password.
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody @Valid TeacherRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.ok(teacherService.update(id, request)));
    }

    // THÊM: soft-delete, không xóa bản ghi khỏi DB.
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    // THÊM: khôi phục cả Teacher và User.
    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> restore(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(teacherService.restore(id)));
    }
}