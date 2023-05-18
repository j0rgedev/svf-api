package com.integrador.svfapi.service.impl;

import com.integrador.svfapi.classes.*;
import com.integrador.svfapi.dto.addStudentBody.RepresentativeInfoDTO;
import com.integrador.svfapi.dto.addStudentBody.StudentInfoDTO;
import com.integrador.svfapi.dto.getAllStudents.StudentListDTO;
import com.integrador.svfapi.dto.studentInformation.EnrolledStudentDTO;
import com.integrador.svfapi.dto.studentInformation.NotEnrolledStudent;
import com.integrador.svfapi.dto.updateStudentBody.UpdateStudentInfoDTO;
import com.integrador.svfapi.exception.BusinessException;
import com.integrador.svfapi.repository.EnrollmentRepository;
import com.integrador.svfapi.repository.RepresentativesRepository;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.repository.UsersRepository;
import com.integrador.svfapi.service.StudentService;
import com.integrador.svfapi.utils.JwtUtil;
import com.integrador.svfapi.utils.PasswordEncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class StudentServiceIMPL implements StudentService {

    private final JwtUtil jwtUtil;
    private final StudentRepository studentRepository;
    private final RepresentativesRepository representativesRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UsersRepository usersRepository;

    private final PasswordEncryption passwordEncryption;

    @Autowired
    public StudentServiceIMPL(
            JwtUtil jwtUtil,
            StudentRepository studentRepository,
            RepresentativesRepository representativesRepository, EnrollmentRepository enrollmentRepository,
            UsersRepository usersRepository, PasswordEncryption passwordEncryption) {
        this.jwtUtil = jwtUtil;
        this.studentRepository = studentRepository;
        this.representativesRepository = representativesRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.usersRepository = usersRepository;
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
                    student.getLastName(),
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
                    student.getLastName(),
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
                    student.getNames() + " " + student.getLastName(),
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
    public ResponseEntity<ResponseFormat> addStudent(String token, StudentInfoDTO studentInfoDTO, RepresentativeInfoDTO representativeInfoDTO) {
        String studentCod = jwtUtil.extractUsername(token);
        if (jwtUtil.validateToken(token, studentCod)) {
            Users newUsers = new  Users(generateNextUserId(),false);
            String salt = passwordEncryption.getSaltvalue(30);
            int currentYear = LocalDate.now().getYear();
            String defaultPassword = createDefaultPasswordFormat(studentInfoDTO.getDni(), studentInfoDTO.getNames(), currentYear);
            String encryptedPassword = passwordEncryption.generateSecurePassword(defaultPassword, salt);
            Student newStudent = new Student(
                    generateNextStudentCod(),
                    studentInfoDTO.getNames(),
                    studentInfoDTO.getLastNames(),
                    studentInfoDTO.getBirthday(),
                    encryptedPassword,
                    salt,
                    studentInfoDTO.getDni(),
                    studentInfoDTO.getAddress(),
                    studentInfoDTO.getEmail(),
                    studentInfoDTO.getPhoneNumber(),
                    studentInfoDTO.getCurrentGrade(),
                    studentInfoDTO.getCurrentLevel(),
                    false,
                    newUsers
            );

            Representatives newRepresentatives= new Representatives(
                    representativeInfoDTO.getDni(),
                    representativeInfoDTO.getNames(),
                    representativeInfoDTO.getLastNames(),
                    representativeInfoDTO.getBirthday(),
                    representativeInfoDTO.getAddress(),
                    representativeInfoDTO.getEmail(),
                    representativeInfoDTO.getPhoneNumber(),
                    "To be decided"
            );

            Map<String, String> data = new HashMap<>();
            data.put("studentCode", newStudent.getStudentCod());
            data.put("defaultPassword", defaultPassword);
            usersRepository.save(newUsers);
            studentRepository.save(newStudent);
            representativesRepository.save(newRepresentatives);
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
                foundStudent.setNames(updateStudentInfoDTO.getNames());
                foundStudent.setLastName(updateStudentInfoDTO.getLastName());
                foundStudent.setBirthday(updateStudentInfoDTO.getBirthday());
                foundStudent.setDni(updateStudentInfoDTO.getDni());
                foundStudent.setAddress(updateStudentInfoDTO.getAddress());
                foundStudent.setEmail(updateStudentInfoDTO.getEmail());
                foundStudent.setPhone(foundStudent.getPhone());

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

    protected String generateNextUserId() {
        // Obtener el último ID registrado en la base de datos
        String lastId = usersRepository.findLastUserId();

        // Extraer el número de secuencia del último ID
        int sequenceNumber = extractSequenceNumber(lastId);

        // Generar el próximo número de secuencia y combinarlo con la parte fija del formato
        String nextId = "U" + (sequenceNumber + 1);

        // Verificar la disponibilidad del ID en la base de datos
        while (usersRepository.existsById(nextId)) {
            sequenceNumber++;
            nextId = "U" + (sequenceNumber + 1);
        }

        return nextId;
    }

    private int extractSequenceNumber(String userId) {
        // Extraer el número de secuencia de un ID en base al formato conocido
        String sequence = userId.substring(1); // Excluir la parte fija 'U'
        return Integer.parseInt(sequence);
    }

    public String generateNextStudentCod() {
        // Obtener el último ID registrado en la base de datos
        String lastId = studentRepository.findLastStudentCod();

        // Extraer el número de secuencia del último ID
        int sequenceNumber = extractSequenceNumber3(lastId);

        // Generar el próximo número de secuencia y combinarlo con la parte fija del formato
        String nextId = "SVF" + String.format("%04d", sequenceNumber + 1);

        // Verificar la disponibilidad del ID en la base de datos
        while (studentRepository.existsById(nextId)) {
            sequenceNumber++;
            nextId = "SVF" + String.format("%04d", sequenceNumber + 1);
        }

        return nextId;
    }

    private int extractSequenceNumber3(String studentId) {
        // Extraer el número de secuencia de un ID en base al formato conocido
        String sequence = studentId.substring(3); // Excluir la parte fija 'SVF'
        return Integer.parseInt(sequence);
    }

    private String createDefaultPasswordFormat(String dni, String firstName, int currentYear) {
        return String.format("%d%s%s", currentYear, firstName.toLowerCase(), dni);
    }
}
