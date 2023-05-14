package com.integrador.svfapi.service.impl;

import com.integrador.svfapi.classes.Enrollment;
import com.integrador.svfapi.classes.Representatives;
import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.dto.studentInformation.EnrolledStudentDTO;
import com.integrador.svfapi.dto.studentInformation.StudentWithOutEnrollmentDTO;
import com.integrador.svfapi.dto.getAllStudents.StudentDTO;
import com.integrador.svfapi.repository.EnrollmentRepository;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.service.StudentService;
import com.integrador.svfapi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentServiceIMPL implements StudentService {

    private final JwtUtil jwtUtil;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public StudentServiceIMPL(
            JwtUtil jwtUtil,
            StudentRepository studentRepository,
            EnrollmentRepository enrollmentRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public ResponseEntity<ResponseFormat> studentInformation(String token) {
        String studentCod = jwtUtil.extractUsername(token);
        Student student = studentRepository.getReferenceById(studentCod);
        Optional <Enrollment> result = Optional.ofNullable(enrollmentRepository.findByStudentCod(studentCod));
        if (result.isPresent()) {
            Enrollment foundEnrollment = result.get();
            EnrolledStudentDTO enrolledStudentDTO = new EnrolledStudentDTO(
                    studentCod,
                    student.getNames(),
                    student.getLastName(),
                    foundEnrollment.getEnrollmentId());

            return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(), enrolledStudentDTO));

        } else {
            String[] newLevelAndGrade;
            newLevelAndGrade = calculateNewLevelAndGrade(student.getCurrentGrade(), student.getCurrentLevel());
            student.setCurrentLevel(newLevelAndGrade[1]);
            student.setCurrentGrade(newLevelAndGrade[0].charAt(0));
            StudentWithOutEnrollmentDTO studentWithOutEnrollmentDTO = new StudentWithOutEnrollmentDTO(
                    studentCod,
                    student.getNames(),
                    student.getLastName(),
                    student.getDni(),
                    student.getCurrentLevel(),
                    student.getCurrentGrade());

            return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(), studentWithOutEnrollmentDTO));

        }
    }

    @Override
    public ResponseEntity<ResponseFormat> getAllStudents() {

        List<Student> allStudents = studentRepository.findAll();
        List<StudentDTO> allStudentsDTO = new ArrayList<>();

        for (Student student: allStudents) {
            Optional<Enrollment> result = Optional.ofNullable(enrollmentRepository.findByStudentCod(student.getStudentCod()));
            boolean isEnrolled;
            isEnrolled = result.isPresent();

            StudentDTO studentDTO = new StudentDTO(
                    student.getStudentCod(),
                    student.getNames() + " " + student.getLastName(),
                    student.getBirthday(), isEnrolled);
            allStudentsDTO.add(studentDTO);
        }

        return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), allStudentsDTO));
    }

    @Override
    public ResponseEntity<ResponseFormat> getStudentById(String studentCod) {

        Student student = studentRepository.findByStudentCod(studentCod);

        return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), student));
    }

    @Override
    public ResponseEntity<ResponseFormat> searchStudent(String query) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseFormat> addStudent(Student student, Representatives representatives) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseFormat> updateStudent(String studentCod) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseFormat> deleteStudent(String studentCod) {
        return null;
    }

    /*
        Functions for studentInformation
    */
    protected String[] calculateNewLevelAndGrade(char currentGrade, String currentLevel) {

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
