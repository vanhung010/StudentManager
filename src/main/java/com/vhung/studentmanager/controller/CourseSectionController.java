package com.vhung.studentmanager.controller;

import com.vhung.studentmanager.dto.request.CourseSectionRequestDTO;
import com.vhung.studentmanager.dto.response.ApiResponse;
import com.vhung.studentmanager.dto.response.CourseSectionResponseDTO;
import com.vhung.studentmanager.dto.response.PageResponse;
import com.vhung.studentmanager.service.CourseSectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course-section")
public class CourseSectionController {

    private final CourseSectionService courseSectionService;

    // GET all
    @GetMapping()
    public ResponseEntity<ApiResponse<PageResponse<CourseSectionResponseDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) String status
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<CourseSectionResponseDTO> data = courseSectionService.getAll(semesterId, courseId, teacherId, status, pageable);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseSectionResponseDTO>> get(@PathVariable Long id) {
        CourseSectionResponseDTO data = courseSectionService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }


    // POST
    @PostMapping
    public ResponseEntity<ApiResponse<CourseSectionResponseDTO>> create(
            @RequestBody @Valid CourseSectionRequestDTO request
    ) {
        CourseSectionResponseDTO data = courseSectionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Mở lớp học phần thành công", data));
    }
    //Đóng
    @PatchMapping("/{id}/close")
    public ResponseEntity<ApiResponse<CourseSectionResponseDTO>> close(@PathVariable Long id){
        CourseSectionResponseDTO data = courseSectionService.close(id);

        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    //Xóa
    @DeleteMapping("/{id}/deleted")
    public ResponseEntity<ApiResponse<CourseSectionResponseDTO>> deleted(@PathVariable Long id){
        CourseSectionResponseDTO data = courseSectionService.deleted(id);

        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    //Khôi phục
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<CourseSectionResponseDTO>> restore(@PathVariable Long id){
        CourseSectionResponseDTO data = courseSectionService.restore(id);

        return ResponseEntity.ok(ApiResponse.ok("Khôi phục lớp học phần thành công", data));
    }

    //Mở
    @PatchMapping("/{id}/open")
    public ResponseEntity<ApiResponse<CourseSectionResponseDTO>> open(@PathVariable Long id){
        CourseSectionResponseDTO data = courseSectionService.open(id);

        return ResponseEntity.ok(ApiResponse.ok("Mở lớp học phần thành công", data));
    }
}
