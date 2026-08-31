package com.vhung.studentmanager.controller;

import com.vhung.studentmanager.dto.request.CourseRequestDTO;
import com.vhung.studentmanager.dto.response.ApiResponse;
import com.vhung.studentmanager.dto.response.CourseResponseDTO;
import com.vhung.studentmanager.dto.response.PageResponse;
import com.vhung.studentmanager.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // THÊM: danh sách môn học.
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CourseResponseDTO>>> getAll(
            @RequestParam(defaultValue = "active") String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("name").ascending()
        );

        return ResponseEntity.ok(
                ApiResponse.ok(courseService.getAll(
                        status,
                        keyword,
                        departmentId,
                        pageable
                ))
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(courseService.get(id)));
    }


    @PostMapping

    public ResponseEntity<ApiResponse<CourseResponseDTO>> create(
            @Valid @RequestBody CourseRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(courseService.create(request)));
    }

    // THÊM: cập nhật môn học.
    @PutMapping("/{id}")

    public ResponseEntity<ApiResponse<CourseResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequestDTO request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(courseService.update(id, request)));
    }

    // THÊM: soft-delete môn học.
    @DeleteMapping("/{id}")

    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    // THÊM: khôi phục môn học.
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> restore(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(courseService.restore(id)));
    }
}

