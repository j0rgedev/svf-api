package com.integrador.svfapi.service;

import com.integrador.svfapi.classes.Enrollment;
import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.dto.EnrolledStudentDTO;
import com.integrador.svfapi.dto.getAllStudents.StudentListDTO;
import com.integrador.svfapi.repository.EnrollmentRepository;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.integrador.svfapi.dto.StudentDTO;

import java.util.*;

@Service
public class StudentServiceImpl implements StudentServiceInterface {

    private final JwtUtil jwtUtil;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public StudentServiceImpl(
            JwtUtil jwtUtil,
            StudentRepository studentRepository,
            EnrollmentRepository enrollmentRepository) {
        this.jwtUtil = jwtUtil;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    /*
     * ENROLLMENT PROCESS FUNCTIONS
     */

    /**
     * Get the student information by the student code which is got from the token.
     * If the student is enrolled, it returns a EnrolledStudentDTO object,
     * otherwise it returns a StudentDTO object.
     *
     * @param token Authentication token.
     * @return ResponseEntity with a custom ResponseFormat object.
     */
    @Override
    public ResponseEntity<ResponseFormat> studentInformation(String token) {
        String studentCod = jwtUtil.extractUsername(token);
        Student student = studentRepository.getReferenceById(studentCod);
        Optional<Enrollment> studentEnrollment = Optional.ofNullable(enrollmentRepository.findByStudentCod(studentCod)); // Get the enrollment information
        Object studentInformation; // Student information object
        if (studentEnrollment.isPresent()) { // Student is enrolled
            Enrollment foundEnrollment = studentEnrollment.get();
            studentInformation = new EnrolledStudentDTO(
                    student.getStudentCod(),
                    student.getNames(),
                    student.getLastName(),
                    foundEnrollment.getEnrollmentId());
        } else { // Student is not enrolled
            String[] newLevelAndGrade = calculateNewLevelAndGrade(
                    student.getCurrentGrade(),
                    student.getCurrentLevel()
            ); // Get the new level and grade
            student.setCurrentLevel(newLevelAndGrade[1]);
            student.setCurrentGrade(newLevelAndGrade[0].charAt(0));
            studentInformation = new StudentDTO(
                    student.getStudentCod(),
                    student.getNames(),
                    student.getLastName(),
                    student.getDni(),
                    student.getCurrentLevel(),
                    student.getCurrentGrade()
            );
        }
        String RESPONSE_MESSAGE = "Información del estudiante obtenida correctamente"; // Custom response message
        return ResponseEntity.ok().body(
                new ResponseFormat(
                        HttpStatus.OK.value(),
                        RESPONSE_MESSAGE,
                        studentInformation
                ));
    }

    @Override
    public String[] calculateNewLevelAndGrade(char currentGrade, String currentLevel) {
        String[] newLevelAndGrade = new String[2];

        if (currentGrade == '6') {
            newLevelAndGrade[0] = "1";
            newLevelAndGrade[1] = "Secundaria";
        } else if (currentGrade == '5' && currentLevel.equals("Inicial")) {
            newLevelAndGrade[0] = "1";
            newLevelAndGrade[1] = "Primaria";
        } else if (currentGrade == '5' && currentLevel.equals("Secundaria")) {
            newLevelAndGrade[0] = "5";
            newLevelAndGrade[1] = "Secundaria";
        } else {
            int newGrade = currentGrade - '0';
            newLevelAndGrade[0] = String.valueOf(newGrade + 1);
            newLevelAndGrade[1] = currentLevel;
        }
        return newLevelAndGrade;
    }


    /*
     * ADMIN INTRANET FUNCTIONS
     */
    @Override
    public ResponseEntity<ResponseFormat> getAllStudents() {

        List<Student> allStudents = studentRepository.findAll();
        List<StudentListDTO> allStudentsDTO = new ArrayList<>();

        for (Student student : allStudents) {
            Optional<Enrollment> result = Optional.ofNullable(enrollmentRepository.findByStudentCod(student.getStudentCod()));
            boolean isEnrolled;
            isEnrolled = result.isPresent();

            StudentListDTO studentListDTO = new StudentListDTO(
                    student.getStudentCod(),
                    student.getNames() + " " + student.getLastName(),
                    student.getBirthday(), isEnrolled);
            allStudentsDTO.add(studentListDTO);
        }

        return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), allStudentsDTO));
    }

    @Override
    public ResponseEntity<ResponseFormat> getStudentById(String studentCod) {

        Student student = studentRepository.findByStudentCod(studentCod);

        return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), student));
    }


}
