package com.vhung.studentmanager.dto.response;

import com.vhung.studentmanager.entity.enums.SectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseSectionResponseDTO {
    private Long id;
    private String sectionCode;

    private Long courseId;
    private String courseCode;
    private String courseName;

    private Long semesterId;
    private String semesterName;

    private Long teacherId;
    private String teacherName;

    private Integer maxStudents;
    private Integer enrolledCount;

    private SectionStatus status;
}
