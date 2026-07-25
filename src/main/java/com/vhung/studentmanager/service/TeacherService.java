package com.vhung.studentmanager.service;

import com.vhung.studentmanager.dto.response.PageResponse;
import com.vhung.studentmanager.dto.response.TeacherResponseDTO;
import com.vhung.studentmanager.entity.Teacher;
import com.vhung.studentmanager.exception.AppException;
import com.vhung.studentmanager.repository.TeacherRepository;
import com.vhung.studentmanager.specification.TeacherSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherResponseDTO getTeacherById(Long id, Authentication authentication) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy giảng viên với Id: " + id));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isSelf = teacher.getUser().getUserName().equals(authentication.getName());

        if (!isAdmin && !isSelf) {
            throw new AppException(HttpStatus.FORBIDDEN, "Không có quyền xem thông tin giảng viên khác");
        }
        return TeacherResponseDTO.fromEntity(teacher);
    }

    public PageResponse<TeacherResponseDTO> getTeachers(String name, Long departmentId, Pageable pageable) {
        Specification<Teacher> spec = Specification
                .where(TeacherSpecification.hasName(name))
                .and(TeacherSpecification.hasDepartmentId(departmentId));

        Page<Teacher> teacherPage = teacherRepository.findAll(spec, pageable);
        Page<TeacherResponseDTO> dtoPage = teacherPage.map(TeacherResponseDTO::fromEntity);

        return PageResponse.from(dtoPage);
    }

    public TeacherResponseDTO getCurrentTeacherByUserName(String username) {
        Teacher teacher = teacherRepository.findByUser_Username(username)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giảng viên với username: " + username));
        return TeacherResponseDTO.fromEntity(teacher);
    }
}