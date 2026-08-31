package com.vhung.studentmanager.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SemesterResponseDTO {
   private Long id;

    private String semesterCode;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate regStartDate;

    private LocalDate regEndDate;

    private Boolean isActive = false;
}
