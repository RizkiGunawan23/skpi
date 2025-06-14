package com.skpijtk.springboot_boilerplate.service;

import com.skpijtk.springboot_boilerplate.dto.request.admin.studentmanagement.CreateStudentRequest;
import com.skpijtk.springboot_boilerplate.dto.request.admin.studentmanagement.UpdateStudentRequest;
import com.skpijtk.springboot_boilerplate.dto.response.admin.studentmanagement.StudentResponse;
import com.skpijtk.springboot_boilerplate.exception.ApiException;
import com.skpijtk.springboot_boilerplate.model.Student;
import com.skpijtk.springboot_boilerplate.model.User;
import com.skpijtk.springboot_boilerplate.repository.StudentRepository;
import com.skpijtk.springboot_boilerplate.repository.UserRepository;
import com.skpijtk.springboot_boilerplate.service.admin.impl.StudentManagementServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentManagementServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentManagementServiceImpl studentManagementService;

    private CreateStudentRequest createRequest;
    private UpdateStudentRequest updateRequest;
    private User user;
    private Student student;

    @BeforeEach
    void setUp() {
        createRequest = new CreateStudentRequest();
        createRequest.setStudentName("Budi");
        createRequest.setEmail("budi@email.com");
        createRequest.setPassword("password");
        createRequest.setNim("250000000");

        updateRequest = new UpdateStudentRequest();
        updateRequest.setStudentName("Budi Update");
        updateRequest.setEmail("budi@gmail.com");
        updateRequest.setNim("250000001");

        user = new User();
        user.setId(1L);
        user.setName("Budi");
        user.setEmail("budi@email.com");
        user.setPassword("encodedPassword");
        user.setRole(User.Role.MAHASISWA);

        student = new Student();
        student.setId(1L);
        student.setSin("123456");
        student.setUser(user);
    }

    @Test
    void createStudent_success() {
        when(studentRepository.existsBySin("250000000")).thenReturn(false);
        when(userRepository.existsByEmail("budi@email.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponse response = studentManagementService.createStudent(createRequest);

        assertNotNull(response);
        assertEquals("Budi", response.getStudentName());
        verify(userRepository).save(any(User.class));
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void createStudent_duplicateEmail_throwsException() {
        when(studentRepository.existsBySin("250000000")).thenReturn(false);
        when(userRepository.existsByEmail("budi@email.com")).thenReturn(true);

        assertThrows(ApiException.class, () -> studentManagementService.createStudent(createRequest));
    }

    @Test
    void updateStudent_success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(userRepository.existsByEmail("budi@gmail.com")).thenReturn(false);
        when(studentRepository.existsBySin("250000001")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponse response = studentManagementService.updateStudent(updateRequest, 1L);

        assertNotNull(response);
        assertEquals("Budi Update", response.getStudentName());
        verify(userRepository).save(any(User.class));
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void updateStudent_notFound_throwsException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ApiException.class, () -> studentManagementService.updateStudent(updateRequest, 1L));
    }
}
