package com.integrador.svfapi.service.impl;

import com.integrador.svfapi.classes.*;
import com.integrador.svfapi.dto.addStudentBody.AddStudentBodyDTO;
import com.integrador.svfapi.dto.getAllStudents.StudentListDTO;
import com.integrador.svfapi.dto.studentInformation.EnrolledStudentDTO;
import com.integrador.svfapi.dto.studentInformation.NotEnrolledStudent;
import com.integrador.svfapi.dto.updateStudentBody.UpdateStudentInfoDTO;
import com.integrador.svfapi.exception.BusinessException;
import com.integrador.svfapi.repository.*;
import com.integrador.svfapi.service.StudentService;
import com.integrador.svfapi.utils.CodeGenerator;
import com.integrador.svfapi.utils.JwtUtil;
import com.integrador.svfapi.utils.PasswordEncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {

    private final JwtUtil jwtUtil;
    private final StudentRepository studentRepository;
    private final RepresentativesRepository representativesRepository;
    private final StudentRepresentativesRepository studentRepresentativesRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CodeGenerator codeGenerator;
    private final PasswordEncryption passwordEncryption;

    @Autowired
    public StudentServiceImpl(
            JwtUtil jwtUtil,
            StudentRepository studentRepository,
            RepresentativesRepository representativesRepository,
            StudentRepresentativesRepository studentRepresentativesRepository,
            EnrollmentRepository enrollmentRepository,
            UserRepository userRepository,
            CodeGenerator codeGenerator,
            PasswordEncryption passwordEncryption) {
        this.jwtUtil = jwtUtil;
        this.studentRepository = studentRepository;
        this.representativesRepository = representativesRepository;
        this.studentRepresentativesRepository = studentRepresentativesRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.codeGenerator = codeGenerator;
        this.passwordEncryption = passwordEncryption;
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
                    student.getLastNames(),
                    foundEnrollment.getEnrollmentId());

            String msg = "El estudiante ya cuenta con una matricula registrada";
            return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), msg, enrolledStudentDTO));

        } else {
            String[] newLevelAndGrade;
            newLevelAndGrade = calculateNewLevelAndGrade(student.getCurrentGrade(), student.getCurrentLevel());
            student.setCurrentLevel(newLevelAndGrade[1]);
            student.setCurrentGrade(newLevelAndGrade[0].charAt(0));
            NotEnrolledStudent notEnrolledStudent = new NotEnrolledStudent(
                    studentCod,
                    student.getNames(),
                    student.getLastNames(),
                    student.getDni(),
                    student.getCurrentLevel(),
                    student.getCurrentGrade());

            String msg = "El estudiante aún no cuenta con una matricula registrada";
            return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), msg, notEnrolledStudent));

        }
    }

    @Override
    public ResponseEntity<ResponseFormat> getAllStudents() {

        List<Student> allStudents = studentRepository.findAll();
        List<StudentListDTO> allStudentsDTO = new ArrayList<>();

        for (Student student: allStudents) {
            Optional<Enrollment> result = Optional.ofNullable(enrollmentRepository.findByStudentCod(student.getStudentCod()));
            boolean isEnrolled;
            isEnrolled = result.isPresent();

            StudentListDTO studentDTO = new StudentListDTO(
                    student.getStudentCod(),
                    student.getNames() + " " + student.getLastNames(),
                    student.getBirthday(), isEnrolled);
            allStudentsDTO.add(studentDTO);
        }

        String msg = "Se envía la lista de estudiantes registrados en el sistema";
        return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), msg, allStudentsDTO));
    }

    @Override
    public ResponseEntity<ResponseFormat> getStudentById(String studentCod) {

        //Student student = studentRepository.findByStudentCod(studentCod);
        Optional<Student> student = studentRepository.findById(studentCod);
                //.orElseThrow(new BusinessException(HttpStatus.NOT_FOUND, "Estudiante no encontrado"));

        String msg = "Este es el resultado de la búsqueda";
        return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), msg, student));
    }

    @Override
    public ResponseEntity<ResponseFormat> addStudent(String token, AddStudentBodyDTO addStudentBodyDTO) {
        String studentCod = jwtUtil.extractUsername(token);
        if (jwtUtil.validateToken(token, studentCod)) {
            User newUser = new User(
                    codeGenerator.generateNextUserId(userRepository.findTopByOrderByUserIdDesc().getUserId()),
                    false,
                    "A10");
            String salt = passwordEncryption.getSaltvalue(30);
            String defaultPassword = codeGenerator.createDefaultPasswordFormat(
                    addStudentBodyDTO.getStudentInfo().getNames(),
                    addStudentBodyDTO.getStudentInfo().getDni());
            String encryptedPassword = passwordEncryption.generateSecurePassword(defaultPassword, salt);
            Student newStudent = new Student(
                    codeGenerator.generateNextStudentCod(studentRepository.findTopByOrderByStudentCodDesc().getStudentCod()),
                    addStudentBodyDTO.getStudentInfo().getNames(),
                    addStudentBodyDTO.getStudentInfo().getLastnames(),
                    addStudentBodyDTO.getStudentInfo().getBirthdate(),
                    encryptedPassword,
                    salt,
                    addStudentBodyDTO.getStudentInfo().getDni(),
                    addStudentBodyDTO.getStudentInfo().getDirection(),
                    addStudentBodyDTO.getStudentInfo().getEmail(),
                    addStudentBodyDTO.getStudentInfo().getPhone(),
                    addStudentBodyDTO.getStudentInfo().getGrade(),
                    addStudentBodyDTO.getStudentInfo().getLevel(),
                    false,
                    newUser
            );

            Representative newRepresentative = new Representative(
                    addStudentBodyDTO.getRepresentativeInfo().getDni(),
                    addStudentBodyDTO.getRepresentativeInfo().getNames(),
                    addStudentBodyDTO.getRepresentativeInfo().getLastnames(),
                    addStudentBodyDTO.getRepresentativeInfo().getBirthdate(),
                    addStudentBodyDTO.getRepresentativeInfo().getDirection(),
                    addStudentBodyDTO.getRepresentativeInfo().getEmail(),
                    addStudentBodyDTO.getRepresentativeInfo().getPhone(),
                    addStudentBodyDTO.getRepresentativeInfo().getOccupation()
            );

            StudentRepresentatives newStudentRepresentatives = new StudentRepresentatives(
                    newStudent,
                    newRepresentative,
                    addStudentBodyDTO.getRepresentativeInfo().getKinship(),
                    codeGenerator.generateNextRelationCod(studentRepresentativesRepository.findTopByOrderByRelationCodeDesc().getRelationCode()));

            Map<String, String> data = new HashMap<>();
            data.put("studentCode", newStudent.getStudentCod());
            data.put("defaultPassword", defaultPassword);
            userRepository.save(newUser);
            studentRepository.save(newStudent);
            representativesRepository.save(newRepresentative);
            studentRepresentativesRepository.save(newStudentRepresentatives);
            String msg = "El registro se realizo correctamente";
            return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), msg, data));

        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

    }

    @Override
    public ResponseEntity<ResponseFormat> updateStudent(String token, String studentCod, UpdateStudentInfoDTO updateStudentInfoDTO) {
        String userCod = jwtUtil.extractUsername(token);
        if (jwtUtil.validateToken(token, userCod)) {
            Optional<Student> result = Optional.ofNullable(studentRepository.findByStudentCod(studentCod));
            if (result.isPresent()) {
                Student foundStudent = studentRepository.findByStudentCod(studentCod);
                foundStudent.setNames(updateStudentInfoDTO.getNewNames());
                foundStudent.setLastNames(updateStudentInfoDTO.getNewLastName());
                foundStudent.setBirthday(updateStudentInfoDTO.getNewBirthday());
                foundStudent.setDni(updateStudentInfoDTO.getNewDni());
                foundStudent.setAddress(updateStudentInfoDTO.getNewAddress());
                foundStudent.setEmail(updateStudentInfoDTO.getNewEmail());
                foundStudent.setPhone(updateStudentInfoDTO.getNewPhone());
                foundStudent.setCurrentGrade(updateStudentInfoDTO.getNewGrade());
                foundStudent.setCurrentLevel(updateStudentInfoDTO.getNewLevel());

                return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), "La informacion se actualizo correctamente", null));
            } else {
                throw new BusinessException(HttpStatus.NOT_FOUND, "El estudiante no existe");
            }

        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    @Override
    public ResponseEntity<ResponseFormat> deleteStudent(String token, String studentCod) {
        String userCod = jwtUtil.extractUsername(token);
        if (jwtUtil.validateToken(token, userCod)) {
            Optional<Student> result = Optional.ofNullable(studentRepository.findByStudentCod(studentCod));
            if (result.isPresent()) {
                studentRepository.deleteById(studentCod);
                String msg = "El estudiante se elimino de la base de datos correctamente";
                return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), msg, null));
            } else {
                throw new BusinessException(HttpStatus.NOT_FOUND, "El estudiante no existe");
            }
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
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
