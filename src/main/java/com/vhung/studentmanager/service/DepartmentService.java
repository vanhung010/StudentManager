package com.vhung.studentmanager.service;

import com.vhung.studentmanager.dto.request.DepartmentRequestDTO;
import com.vhung.studentmanager.dto.response.DepartmentResponseDTO;
import com.vhung.studentmanager.entity.Departments;
import com.vhung.studentmanager.exception.AppException;
import com.vhung.studentmanager.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor //Tujw sinh constructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;


    // lấy danh sách tất cả khoa
    public List<DepartmentResponseDTO> getAllDepartment(){
        return departmentRepository.findAllByIsDeletedFalse().stream()
                .map(this::toDTO)
                .toList();
    }

    public DepartmentResponseDTO create(DepartmentRequestDTO departmentsRequestDTO){
        if(departmentRepository.existsByDepartmentCode(departmentsRequestDTO.getDepartmentCode())){
            throw new AppException(HttpStatus.CONFLICT, "Mã khoa đã tồn tại");
        }
        //Convert từ DTO -> Entity

        Departments departments = new Departments();
        departments.setDepartmentCode(departmentsRequestDTO.getDepartmentCode());
        departments.setName(departmentsRequestDTO.getName());
        departments.setDeleted(false);
        //lưu
        Departments saved = departmentRepository.save(departments);
        //trả về DTO



        return toDTO(saved);
    }

    public DepartmentResponseDTO update(Long id, DepartmentRequestDTO departmentRequestDTO){
        Departments departments = departmentRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy khoa"));

        if(departmentRepository.existsByDepartmentCodeAndIdNot(departmentRequestDTO.getDepartmentCode(), id)) {
            throw new AppException(HttpStatus.CONFLICT, "Mã khoa đã tồn tại");
        }

        departments.setDepartmentCode(departmentRequestDTO.getDepartmentCode());
        departments.setName(departmentRequestDTO.getName());

        return toDTO(departmentRepository.save(departments));
    }

    public void deleted(Long id){
        Departments departments = departmentRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy khoa"));


        departments.setDeleted(true);
        departmentRepository.save(departments);

    }

    public DepartmentResponseDTO toDTO(Departments departments){
        return new DepartmentResponseDTO(
                departments.getId(),
                departments.getName(),
                departments.getDepartmentCode());
    }
}
