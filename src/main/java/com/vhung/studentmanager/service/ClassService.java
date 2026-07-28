package com.vhung.studentmanager.service;

import com.vhung.studentmanager.dto.request.ClassRequestDTO;
import com.vhung.studentmanager.dto.response.ClassResponseDTO;
import com.vhung.studentmanager.entity.Classes;
import com.vhung.studentmanager.entity.Departments;
import com.vhung.studentmanager.entity.Teacher;
import com.vhung.studentmanager.exception.AppException;
import com.vhung.studentmanager.repository.ClassRepository;
import com.vhung.studentmanager.repository.DepartmentRepository;
import com.vhung.studentmanager.repository.StudentRepository;
import com.vhung.studentmanager.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;

    public ClassResponseDTO save(ClassRequestDTO request){
        //Tạo khoa
        Departments departments = departmentRepository.findByIdAndIsDeletedFalse(request.getDepartmentId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "không tìm thấy khoa hoặc khoa đã bị xóa"));
        //Tạo giảng viên
        Teacher teacher = teacherRepository.findByIdAndIsDeletedFalse(request.getAdvisorId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy cố vấn hoặc đã bị xóa"));
        //Tạo Class
        Classes classes = Classes.builder()
                .classCode(request.getClassCode())
                .name(request.getName())
                .department(departments)
                .advisor(teacher)
                .enrollmentYear(request.getEnrollmentYear())
                .isDeleted(false).build();

        Classes classSave = classRepository.save(classes);

        return toDTO(classSave, 0);

    }

    private ClassResponseDTO toDTO(Classes classes, int totalStudent){
        ClassResponseDTO result = ClassResponseDTO.builder()
                .id(classes.getId())
                .classCode(classes.getClassCode())
                .name(classes.getName())
                .departmentCode(classes.getDepartment().getDepartmentCode())
                .fullNameTeacher(classes.getAdvisor().getFullName())
                .totalStudents(totalStudent)
                .enrollmentYear(classes.getEnrollmentYear()).build();

        return result;

    }
}
