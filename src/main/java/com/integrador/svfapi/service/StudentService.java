package com.integrador.svfapi.service;

import com.integrador.svfapi.classes.Enrollment;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.dto.StudentDTO;
import com.integrador.svfapi.dto.StudentWithEnrollmentDTO;
import com.integrador.svfapi.repository.EnrollmentRepository;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class StudentService {

    private final JwtUtil jwtUtil;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public StudentService(
            JwtUtil jwtUtil,
            StudentRepository studentRepository,
            EnrollmentRepository enrollmentRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }
    public ResponseEntity<Map<String, String>> studentInformation (String token) {

        String studentCod = jwtUtil.extractUsername(token);
        Student student = studentRepository.getReferenceById(studentCod);
        Optional <Enrollment> result = Optional.ofNullable(enrollmentRepository.findByStudentCod(studentCod));
        if (result.isPresent()) {
            Enrollment foundEnrollment = result.get();
            StudentWithEnrollmentDTO studentWithEnrollmentDTO = new StudentWithEnrollmentDTO(
                    studentCod,
                    student.getNames(),
                    student.getLastName(),
                    foundEnrollment.getEnrollmentId());

            return ResponseEntity.ok().body(Map.of(
                    "student_cod", studentWithEnrollmentDTO.getStudentCod(),
                    "names", studentWithEnrollmentDTO.getNames(),
                    "lastNames", studentWithEnrollmentDTO.getLastNames(),
                    "enrollmentId", studentWithEnrollmentDTO.getEnrollmentID()));

        } else {
            String[] newLevelAndGrade;
            newLevelAndGrade = calculateNewLevelAndGrade(student.getCurrentGrade(), student.getCurrentLevel());
            student.setCurrentLevel(newLevelAndGrade[1]);
            student.setCurrentGrade(newLevelAndGrade[0].charAt(0));
            StudentDTO studentDTO = new StudentDTO(studentCod,student.getNames(),student.getLastName(),student.getDni(),student.getCurrentLevel(), student.getCurrentGrade());

            return ResponseEntity.ok().body(Map.of(
                    "student_cod", studentDTO.getStudent_cod(),
                    "names", studentDTO.getNames(),
                    "lastnames", studentDTO.getLastnames(),
                    "dni", studentDTO.getDni(),
                    "newLevel", studentDTO.getCurrentLevel(),
                    "newGrade", String.valueOf(studentDTO.getCurrentGrade())));
        }

    }

    /*
        Functions for studentInformation
    */
    public String[] calculateNewLevelAndGrade(char currentGrade, String currentLevel) {

        String[] newLevelAndGrade = new String[2];

        if (currentGrade == '6') {
            newLevelAndGrade[0] = "1";
            newLevelAndGrade[1] = "Secundaria";
        } else
        if (currentGrade == '5' && currentLevel.equals("Inicial")) {
            newLevelAndGrade[0] = "1";
            newLevelAndGrade[1] = "Primaria";
        } else
        if (currentGrade == '5' && currentLevel.equals("Secundaria")) {
            newLevelAndGrade[0] = "5";
            newLevelAndGrade[1] = "Secundaria";
        } else {
            int newGrade = currentGrade - '0';
            newLevelAndGrade[0] = String.valueOf(newGrade + 1);
            newLevelAndGrade[1] = currentLevel;
        }
        return newLevelAndGrade;
    }

}
