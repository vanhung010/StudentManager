package com.vhung.studentmanager.controller;

import com.vhung.studentmanager.dto.request.SemesterRequestDTO;
import com.vhung.studentmanager.dto.response.ApiResponse;
import com.vhung.studentmanager.dto.response.PageResponse;
import com.vhung.studentmanager.dto.response.SemesterResponseDTO;
import com.vhung.studentmanager.service.SemesterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<SemesterResponseDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyWord,
            @RequestParam(required = false) String status) {

        Pageable pageable = PageRequest
                .of(page, size);

        PageResponse<SemesterResponseDTO> data = semesterService.getAll(status, keyWord, pageable);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<SemesterResponseDTO>> setCurrent(@PathVariable Long id){
        SemesterResponseDTO data = semesterService.setCurrent(id);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        semesterService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

}
