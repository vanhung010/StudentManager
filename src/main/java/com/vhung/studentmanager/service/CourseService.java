package com.vhung.studentmanager.service;

import com.vhung.studentmanager.dto.request.CourseRequestDTO;
import com.vhung.studentmanager.dto.response.CourseResponseDTO;
import com.vhung.studentmanager.dto.response.PageResponse;
import com.vhung.studentmanager.entity.Course;
import com.vhung.studentmanager.entity.Departments;
import com.vhung.studentmanager.exception.AppException;
import com.vhung.studentmanager.repository.CourseReposistory;
import com.vhung.studentmanager.repository.DepartmentRepository;
import com.vhung.studentmanager.specification.CourseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseReposistory courseRepository;
    private final DepartmentRepository departmentRepository;

    // THÊM: danh sách môn học có filter + pagination.
    public PageResponse<CourseResponseDTO> getAll(
            String status,
            String keyword,
            Long departmentId,
            Pageable pageable
    ) {
        Specification<Course> specification = Specification
                .where(CourseSpecification.hasStatus(status))
                .and(CourseSpecification.hasKeyword(keyword))
                .and(CourseSpecification.hasDepartmentId(departmentId));

        Page<CourseResponseDTO> page = courseRepository
                .findAll(specification, pageable)
                .map(this::toDTO);

        return PageResponse.from(page);
    }

    // THÊM: lấy chi tiết môn học đang active để mở modal sửa.
    public CourseResponseDTO get(Long id) {
        Course course = courseRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy môn học"
                ));

        return toDTO(course);
    }

    // THÊM: tạo môn học.
    @Transactional
    public CourseResponseDTO create(CourseRequestDTO request) {
        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new AppException(HttpStatus.CONFLICT, "Mã môn học đã tồn tại");
        }

        Departments department = getActiveDepartment(request.getIdDepartment());

        Course course = new Course();
        course.setCourseCode(request.getCourseCode().trim());
        course.setName(request.getName().trim());
        course.setCredits(request.getCredits());
        course.setDescription(normalizeDescription(request.getDescription()));
        course.setDepartment(department);
        course.setIsDeleted(false);

        return toDTO(courseRepository.save(course));
    }

    // THÊM: cập nhật môn học.
    @Transactional
    public CourseResponseDTO update(Long id, CourseRequestDTO request) {
        Course course = courseRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy môn học"
                ));

        if (courseRepository.existsByCourseCodeAndIdNot(request.getCourseCode(), id)) {
            throw new AppException(HttpStatus.CONFLICT, "Mã môn học đã tồn tại");
        }

        Departments department = getActiveDepartment(request.getIdDepartment());

        course.setCourseCode(request.getCourseCode().trim());
        course.setName(request.getName().trim());
        course.setCredits(request.getCredits());
        course.setDepartment(department);
        course.setDescription(normalizeDescription(request.getDescription()));

        return toDTO(courseRepository.save(course));
    }

    // THÊM: soft-delete, không xóa record khỏi DB.
    @Transactional
    public void delete(Long id) {
        Course course = courseRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy môn học"
                ));

        course.setIsDeleted(true);
        courseRepository.save(course);
    }

    // THÊM: khôi phục môn học.
    @Transactional
    public CourseResponseDTO restore(Long id) {
        Course course = courseRepository.findByIdAndIsDeletedTrue(id)
                .orElseThrow(() -> new AppException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy môn học đã xóa"
                ));

        // Khoa phải còn hoạt động thì mới cho khôi phục.
        getActiveDepartment(course.getDepartment().getId());

        course.setIsDeleted(false);
        return toDTO(courseRepository.save(course));
    }

    private Departments getActiveDepartment(Long idDepartment) {
        return departmentRepository.findByIdAndIsDeletedFalse(idDepartment)
                .orElseThrow(() -> new AppException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy khoa hoặc khoa đã bị xóa"
                ));
    }

    private String normalizeDescription(String description) {
        if (description == null || description.isBlank()) {
            return null;
        }
        return description.trim();
    }

    private CourseResponseDTO toDTO(Course course) {
        return CourseResponseDTO.builder()
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .name(course.getName())
                .credits(course.getCredits())
                .departmentId(course.getDepartment().getId())
                .departmentName(course.getDepartment().getName())
                .description(course.getDescription())
                .isDeleted(course.getIsDeleted())
                .build();
    }
}

