package com.vhung.studentmanager.service;

import com.vhung.studentmanager.dto.request.CourseSectionRequestDTO;
import com.vhung.studentmanager.dto.response.CourseSectionResponseDTO;
import com.vhung.studentmanager.dto.response.PageResponse;
import com.vhung.studentmanager.entity.Course;
import com.vhung.studentmanager.entity.CourseSections;
import com.vhung.studentmanager.entity.Semesters;
import com.vhung.studentmanager.entity.Teacher;
import com.vhung.studentmanager.entity.enums.SectionStatus;
import com.vhung.studentmanager.exception.AppException;
import com.vhung.studentmanager.repository.*;
import com.vhung.studentmanager.specification.CourseSectionSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CourseSectionService {

    private final CourseSectionRepository courseSectionsRepository;
    private final CourseReposistory courseReposistory;
    private final SemesterRepository semesterRepository;
    private final TeacherRepository teacherRepository;
    private final EnrollmentRepository enrollmentRepository;

    public PageResponse<CourseSectionResponseDTO> getAll(
            Long semesterId, Long courseId, Long teacherId, String status, Pageable pageable) {
        Specification<CourseSections> spec = Specification
                .where(CourseSectionSpecification.hasSemester(semesterId))
                .and(CourseSectionSpecification.hasCourse(courseId))
                .and(CourseSectionSpecification.hasTeacher(teacherId))
                .and(CourseSectionSpecification.hasStatus(status));
        Page<CourseSectionResponseDTO> page = courseSectionsRepository
                .findAll(spec, pageable)
                .map(s -> toDTO(s, 0));
        return PageResponse.from(page);
    }

    public CourseSectionResponseDTO getById(Long id) {
        CourseSections section = courseSectionsRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy lớp học phần"));
        int enrolledCount = enrollmentRepository.countByCourseSection_Id(id);
        return toDTO(section, enrolledCount);
    }

    @Transactional
    public CourseSectionResponseDTO create(CourseSectionRequestDTO request) {

        // 1. Tồn tại & hợp lệ — không tin ID gửi từ client
        Course course = courseReposistory.findByIdAndIsDeletedFalse(request.getCourseId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy môn học"));

        Semesters semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy học kỳ"));

        Teacher teacher = teacherRepository.findByIdAndIsDeletedFalse(request.getTeacherId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy giảng viên"));

        // 2. Ràng buộc nghiệp vụ
        if (semester.getEndDate() != null && semester.getEndDate().isBefore(LocalDate.now())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Không thể mở lớp học phần cho học kỳ đã kết thúc");
        }

        if (courseSectionsRepository.existsByCourse_IdAndSemester_IdAndTeacher_Id(
                request.getCourseId(), request.getSemesterId(), request.getTeacherId())) {
            throw new AppException(HttpStatus.CONFLICT,
                    "Giảng viên này đã được phân công dạy môn học này trong học kỳ đã chọn");
        }

        // 3. Tự sinh mã lớp học phần
        long count = courseSectionsRepository.countByCourse_IdAndSemester_Id(
                request.getCourseId(), request.getSemesterId());
        String sectionCode = course.getCourseCode() + "-" + String.format("%02d", count + 1);

        // 4. Tạo entity
        CourseSections section = new CourseSections();
        section.setSectionCode(sectionCode);
        section.setCourse(course);
        section.setSemester(semester);
        section.setTeacher(teacher);
        section.setMaxStudents(request.getMaxStudents());
        section.setStatus(SectionStatus.OPEN);

        CourseSections saved = courseSectionsRepository.save(section);

        // Lớp vừa mở
        return toDTO(saved, 0);
    }

    public CourseSectionResponseDTO close(Long id){
        CourseSections courseSections = courseSectionsRepository.findById(id).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy lớp"));
        //Kiểm tra điều kiện
        if(courseSections.getStatus().equals(SectionStatus.CANCELLED)){
            throw new AppException(HttpStatus.BAD_REQUEST, "Lớp học hiện tại đã khóa");
        }
        else if(courseSections.getStatus().equals(SectionStatus.CLOSED)){
            throw new AppException(HttpStatus.BAD_REQUEST, "Lớp học hiện tại đang khóa");
        }

        courseSections.setStatus(SectionStatus.CLOSED);

        CourseSections courseSectionsSave = courseSectionsRepository.save(courseSections);

        int studentEnrollment = enrollmentRepository.countByCourseSection_Id(id);
        return toDTO(courseSectionsSave, studentEnrollment);

    }

    public CourseSectionResponseDTO deleted(Long id){
        CourseSections courseSections = courseSectionsRepository.findById(id).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy lớp học phần"));
        if(courseSections.getStatus().equals(SectionStatus.CANCELLED)){
            throw new AppException(HttpStatus.BAD_REQUEST, "Lớp học hiện tại đã bị xóa");
        }

        int enrolledCount = enrollmentRepository.countByCourseSection_Id(id);

        if(enrolledCount != 0) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Sĩ số lớp học phần hiện tại lớn hơn 0");
        }

        courseSections.setStatus(SectionStatus.CANCELLED);

        CourseSections courseSectionsSave = courseSectionsRepository.save(courseSections);

        return  toDTO(courseSectionsSave, 0);
    }

    public CourseSectionResponseDTO open(Long id){
        CourseSections courseSections = courseSectionsRepository.findById(id).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy lớp"));

        if(courseSections.getStatus().equals(SectionStatus.OPEN)){
            throw new AppException(HttpStatus.BAD_REQUEST, "Lớp học phần hiện tại đang mở");
        }
        else if(courseSections.getStatus().equals(SectionStatus.CANCELLED)){
            throw new AppException(HttpStatus.BAD_REQUEST, "Lớp học phần hiện tại đã đóng, không thể mở");
        }

        courseSections.setStatus(SectionStatus.OPEN);

        CourseSections courseSectionsSave = courseSectionsRepository.save(courseSections);

        return toDTO(courseSectionsSave, enrollmentRepository.countByCourseSection_Id(id));
    }

    public CourseSectionResponseDTO restore(Long id) {
        CourseSections courseSections = courseSectionsRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy lớp học phần"));

        if (!courseSections.getStatus().equals(SectionStatus.CANCELLED)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Lớp học phần chưa bị xóa, không thể khôi phục");
        }

        courseSections.setStatus(SectionStatus.OPEN);

        CourseSections courseSectionsSave = courseSectionsRepository.save(courseSections);

        return toDTO(courseSectionsSave, enrollmentRepository.countByCourseSection_Id(id));
    }

    private CourseSectionResponseDTO toDTO(CourseSections section, Integer enrolledCount) {
        CourseSectionResponseDTO dto = new CourseSectionResponseDTO();
        dto.setId(section.getId());
        dto.setSectionCode(section.getSectionCode());
        dto.setMaxStudents(section.getMaxStudents());
        dto.setStatus(section.getStatus());
        dto.setEnrolledCount(enrolledCount);

        if (section.getCourse() != null) {
            dto.setCourseId(section.getCourse().getId());
            dto.setCourseCode(section.getCourse().getCourseCode());
            dto.setCourseName(section.getCourse().getName());
        }
        if (section.getSemester() != null) {
            dto.setSemesterId(section.getSemester().getId());
            dto.setSemesterName(section.getSemester().getName());
        }
        if (section.getTeacher() != null) {
            dto.setTeacherId(section.getTeacher().getId());
            dto.setTeacherName(section.getTeacher().getFullName());
        }
        return dto;
    }
}
