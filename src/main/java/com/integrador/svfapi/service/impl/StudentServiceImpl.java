package com.integrador.svfapi.service.impl;

import com.integrador.svfapi.classes.*;
import com.integrador.svfapi.dto.PensionsPayment;
import com.integrador.svfapi.dto.StudentPensionDTO;
import com.integrador.svfapi.dto.addStudentBody.AddStudentBodyDTO;
import com.integrador.svfapi.dto.getAllStudents.SingleStudentDTO;
import com.integrador.svfapi.dto.getAllStudents.StudentListDTO;
import com.integrador.svfapi.dto.studentInformation.EnrolledStudentDTO;
import com.integrador.svfapi.dto.studentInformation.NotEnrolledStudent;
import com.integrador.svfapi.dto.updateStudentBody.UpdateStudentInfoDTO;
import com.integrador.svfapi.exception.BusinessException;
import com.integrador.svfapi.repository.*;
import com.integrador.svfapi.service.StudentService;
import com.integrador.svfapi.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {

    private final JwtUtil jwtUtil;
    private final JMail jMail;
    private final StudentRepository studentRepository;
    private final RepresentativesRepository representativesRepository;
    private final ReceiptRepository receiptRepository;
    private final ReceiptPensionRepository receiptPensionRepository;
    private final PaymentsRepository paymentsRepository;
    private final StudentRepresentativesRepository studentRepresentativesRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PensionRepository pensionRepository;
    private final UserRepository userRepository;
    private final CodeGenerator codeGenerator;
    private final PasswordEncryption passwordEncryption;

    @Autowired
    public StudentServiceImpl(
            JwtUtil jwtUtil,
            JMail jMail,
            StudentRepository studentRepository,
            RepresentativesRepository representativesRepository,
            ReceiptRepository receiptRepository, ReceiptPensionRepository receiptPensionRepository, PaymentsRepository paymentsRepository, StudentRepresentativesRepository studentRepresentativesRepository,
            EnrollmentRepository enrollmentRepository,
            PensionRepository pensionRepository,
            UserRepository userRepository,
            CodeGenerator codeGenerator,
            PasswordEncryption passwordEncryption) {
        this.jwtUtil = jwtUtil;
        this.jMail = jMail;
        this.studentRepository = studentRepository;
        this.representativesRepository = representativesRepository;
        this.receiptRepository = receiptRepository;
        this.receiptPensionRepository = receiptPensionRepository;
        this.paymentsRepository = paymentsRepository;
        this.studentRepresentativesRepository = studentRepresentativesRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.pensionRepository = pensionRepository;
        this.userRepository = userRepository;
        this.codeGenerator = codeGenerator;
        this.passwordEncryption = passwordEncryption;
    }

    /*
     * FUNCIONES PARA EL CRUD DE ESTUDIANTES
     */

    /**
     * Este método devuelve la información del estudiante a traves del studentCode que se obtiene a tráves del token.
     * Si el estudiante esta matriculado, retornara un objeto EnrolledStudentDTO,
     * de otra manera retornara un objeto StudentDTO.
     *
     * @param token Token de autenticación.
     * @return ResponseEntity con un objeto personalizado para la respuesta de tipo ResponseFormat.
     */
    @Override
    public ResponseEntity<ResponseFormat> studentInformation(String token) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.STUDENT)) {
            String studentCod = tokenValidationResult.code();
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
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

    }

    /**
     * Este método se usa para obtener la información de todos los estudiantes excluyendo
     * ciertos campos con informacion sensible como contraseñas, salt, entre otros.
     *
     * @return ResponseEntity con un objeto personalizado para la respuesta de tipo ResponseFormat.
     */
    @Override
    public ResponseEntity<ResponseFormat> getAllStudents(String token) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.ADMIN)){
            List<Student> allStudents = studentRepository.findActiveStudents();
            List<StudentListDTO> allStudentsDTO = new ArrayList<>();

            for (Student student: allStudents) {
                StudentListDTO studentListDTO = new StudentListDTO(
                        student.getStudentCod(),
                        student.getNames() + " " + student.getLastNames(),
                        student.getBirthday(),
                        student.isEnrolled());
                allStudentsDTO.add(studentListDTO);

            }
            String msg = "Se envía la lista de estudiantes registrados en el sistema";
            return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), msg, allStudentsDTO));
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

    }

    /**
     * Este método se usa para obtener la información de un unico estudiante usando
     * el código del estudiante como parámetro de búsqueda.
     *
     * @param studentCod Código del estudiante
     * @return ResponseEntity con un objeto personalizado para la respuesta de tipo ResponseFormat.
     */
    @Override
    public ResponseEntity<ResponseFormat> getStudentById(String token, String studentCod) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.ADMIN)) {
            Optional<Student> student = studentRepository.findById(studentCod);
            if(student.isPresent()){
                SingleStudentDTO singleStudentDTO = new SingleStudentDTO(
                        student.get().getStudentCod(),
                        student.get().getNames(),
                        student.get().getLastNames(),
                        student.get().getBirthday(),
                        student.get().getDni(),
                        student.get().getAddress(),
                        student.get().getEmail(),
                        student.get().getPhone(),
                        student.get().getCurrentLevel(),
                        student.get().getCurrentGrade()
                );
                return ResponseEntity.ok().body(new ResponseFormat(
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        singleStudentDTO
                ));
            } else {
                throw new BusinessException(HttpStatus.NOT_FOUND, "El estudiante no existe");
            }
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

    }

    /**
     * Este método se usa para agregar un nuevo registro de un estudiante a la base de datos.
     * Para crear este registro se ingresa como parámetros el token y un objeto DTO que lleva
     * tanto la informacion necesaria para el nuevo registro.
     * El output de este método sera el código del estudiante con su nueva contraseña la cual
     * tendrá el formato por default.
     *
     * @param token Token de autenticación.
     * @param addStudentBodyDTO Objeto con la informacion tanto del estudiante como de su padre o apoderado
     * @return ResponseEntity con un objeto personalizado para la respuesta de tipo ResponseFormat.
     */
    @Override
    public ResponseEntity<ResponseFormat> addStudent(String token, AddStudentBodyDTO addStudentBodyDTO) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.ADMIN)) {
            //Creación del nuevo usuario
            User newUser = new User(
                    codeGenerator.generateNextUserId(userRepository.findTopByOrderByUserIdDesc().getUserId()),
                    true,
                    "A10");
            String salt = passwordEncryption.getSaltvalue(30);
            String defaultPassword = codeGenerator.createDefaultPasswordFormat(
                    addStudentBodyDTO.getStudentInfo().getNames(),
                    addStudentBodyDTO.getStudentInfo().getDni());
            String encryptedPassword = passwordEncryption.generateSecurePassword(defaultPassword, salt);
            //Creación del nuevo estudiante
            Student newStudent = new Student(
                    codeGenerator.generateNextStudentCod(studentRepository.findTopByOrderByStudentCodDesc().getStudentCod()),
                    addStudentBodyDTO.getStudentInfo().getNames(),
                    addStudentBodyDTO.getStudentInfo().getLastnames(),
                    addStudentBodyDTO.getStudentInfo().getBirthdate(),
                    encryptedPassword,
                    salt,
                    addStudentBodyDTO.getStudentInfo().getDni(),
                    addStudentBodyDTO.getStudentInfo().getGender(),
                    addStudentBodyDTO.getStudentInfo().getDirection(),
                    addStudentBodyDTO.getStudentInfo().getEmail(),
                    addStudentBodyDTO.getStudentInfo().getPhone(),
                    addStudentBodyDTO.getStudentInfo().getGrade(),
                    addStudentBodyDTO.getStudentInfo().getLevel(),
                    false,
                    newUser
            );
            //Creación del nuevo padre o apoderado
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
            //Creación de la relación entre el estudiante y su apoderado
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
            try {
                jMail.sendMail(
                        newStudent.getEmail(),
                        "Bienvenido a la familia SVF",
                        "Su código de estudiante es: " + newStudent.getStudentCod() + "\nSu contraseña es: " + defaultPassword);
                data.put("emailStatus", "Correo enviado");
            } catch (Exception e) {
                data.put("emailStatus", "Correo no enviado");
            }
            return ResponseEntity.ok().body(new ResponseFormat(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    data));
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

    }

    /**
     * La finalidad de este método es de actualizar la información del registro en la base de datos de un estudiante.
     * Para ello se ingresan como parámetros el token, el código del estudiante y un objeto DTO.
     *
     * @param token Token de autenticación.
     * @param studentCod Código del estudiante.
     * @param updateStudentInfo Objeto DTO que lleva la información a actualizar del registro.
     * @return ResponseEntity con un objeto personalizado para la respuesta de tipo ResponseFormat.
     */
    @Override
    public ResponseEntity<ResponseFormat> updateStudent(String token, String studentCod, UpdateStudentInfoDTO updateStudentInfo) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.ADMIN)) {
            //Validación de la existencia del registro
            Optional<Student> result = Optional.ofNullable(studentRepository.findByStudentCod(studentCod));
            if (result.isPresent()) {
                Student foundStudent = result.get();
                foundStudent.setBirthday(updateStudentInfo.getNewBirthday());
                foundStudent.setDni(updateStudentInfo.getNewDni());
                foundStudent.setAddress(updateStudentInfo.getNewAddress());
                foundStudent.setEmail(updateStudentInfo.getNewEmail());
                foundStudent.setPhone(updateStudentInfo.getNewPhone());
                foundStudent.setCurrentGrade(updateStudentInfo.getNewGrade());
                foundStudent.setCurrentLevel(updateStudentInfo.getNewLevel());
                studentRepository.save(foundStudent);
                return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), "La informacion se actualizo correctamente", null));
            } else {
                throw new BusinessException(HttpStatus.NOT_FOUND, "El estudiante no existe");
            }

        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    /**
     * La finalidad de este método es la de iniciar un proceso de eliminación de un registro
     * de estudiante de la base de datos.
     * El proceso inciará por cambiar el estado de usuario del estudiante a inactivo y se otorgara
     * un plazo de 30 días para que la eliminación sea efectiva.
     *
     * @param token Token de autenticación.
     * @param studentCod Código del estudiante.
     * @return ResponseEntity con un objeto personalizado para la respuesta de tipo ResponseFormat.
     */
    @Override
    public ResponseEntity<ResponseFormat> deleteStudent(String token, String studentCod) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.ADMIN)) {
            //Validación de la existencia del registro
            Optional<Student> result = Optional.ofNullable(studentRepository.findByStudentCod(studentCod));
            if (result.isPresent()) {
                Student foundStudent = result.get();
                User user = userRepository.getReferenceById(foundStudent.getUser().getUserId());
                user.setActive(false);
                userRepository.save(user);
                String msg = "El estado del usuario fue cambiado a inactivo y" +
                            " su cuenta sera borrada del registro en 30 días. ";
                return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), msg, null));
            } else {
                throw new BusinessException(HttpStatus.NOT_FOUND, "El estudiante no existe");
            }
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }


    @Override
    public ResponseEntity<ResponseFormat> getStudent(String token){
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.STUDENT)) {
            Student student = studentRepository.findById(tokenValidationResult.code())
                    .orElseThrow(()-> new BusinessException(HttpStatus.NOT_FOUND, "El estudiante no existe"));

            return ResponseEntity.ok().body(new ResponseFormat(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    new StudentInformation(
                            student.getNames(),
                            student.getLastNames()
                    )
            ));
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    private record StudentInformation(
            String studentName,
            String studentLastName
    ){}

    /**
     *  Método que permite obtener todas las pensiones de un estudiante.
     *
     * @param token Token de autenticación.
     * @return ResponseEntity con un objeto personalizado para la respuesta de tipo ResponseFormat.
     */
    @Override
    public ResponseEntity<ResponseFormat> studentPensions(String token, boolean pensionStatus) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.STUDENT)) {
            Student student = studentRepository.findByStudentCod(tokenValidationResult.code());
            if(!pensionStatus){
                return getPensionsByStatus(student, false);
            } else {
                return getPensionsByStatus(student, true);
            }
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    @Override
    public ResponseEntity<ResponseFormat> payPension(String token, PensionsPayment pensionsPayment){
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.STUDENT)) {
            String studentCod = tokenValidationResult.code();
            String lastReceiptCode = receiptRepository.findTopByOrderByReceiptCodDesc().getReceiptCod();
            String newReceiptCode = CodeGenerator.generateNextReceiptCode(lastReceiptCode);
            List<Pension> studentPensions = pensionRepository.findAllByStudent_StudentCod(studentCod);
            List<Integer> paidPensionsCodes = pensionsPayment.pensionCod();
            long totalAmount = 0;
            // Update pensions status
            for(Pension pension : studentPensions){
                if(paidPensionsCodes.contains(pension.getPensionCod())){
                    pension.setStatus(true);
                    pensionRepository.save(pension);
                    totalAmount += pension.getAmount();
                }
            }

            Payments payments = paymentsRepository.findById(pensionsPayment.paymentId()).
                    orElseThrow(()-> new BusinessException(HttpStatus.NOT_FOUND, "El pago no existe"));

            // Create receipt
            Receipt receipt = new Receipt(
                    newReceiptCode,
                    Timestamp.valueOf(LocalDateTime.now()),
                    totalAmount,
                    payments,
                    LocalDate.now(),
                    null
            );
            receiptRepository.save(receipt);

            // Add receipt pensions rows
            for(Pension pension : studentPensions){
                if(paidPensionsCodes.contains(pension.getPensionCod())){
                    ReceiptPension receiptPension = new ReceiptPension();
                    receiptPension.setReceipt(receipt);
                    receiptPension.setPension(pension);
                    receiptPensionRepository.save(receiptPension);
                }
            }

            return ResponseEntity.ok().body(new ResponseFormat(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    null
            ));

        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "No autorizado");
        }
    }


    private ResponseEntity<ResponseFormat> getPensionsByStatus(Student student, boolean paid) {
        List<Pension> studentPensions = pensionRepository.findAllByStudent(student);

        String[] pensionNames = {
                "Pensión de marzo",
                "Pensión de abril",
                "Pensión de mayo",
                "Pensión de junio",
                "Pensión de julio",
                "Pensión de agosto",
                "Pensión de septiembre",
                "Pensión de octubre",
                "Pensión de noviembre",
                "Pensión de diciembre"
        };

        if (studentPensions.isEmpty()) {
            return ResponseEntity.ok().body(new ResponseFormat(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    new StudentPensionDTO(
                            0,
                            new ArrayList<>()
                    )
            ));
        }

        List<PensionDTO> pensions = new ArrayList<>();
        List<PaidPensionDto> paidPensions = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        double totalDebt = 0;
        int i = -1;
        for (Pension pension : studentPensions) {
            i++;
            boolean isPaid = pension.isStatus();
            if (paid && isPaid) {
                String pensionName = pensionNames[i];

                String receiptCode = getReceiptCodeFromPension(pension);

                PaidPensionDto paidPensionDto = new PaidPensionDto(
                        pension.getPensionCod(),
                        pensionName,
                        pension.getAmount(),
                        pension.getDueDate(),
                        receiptCode
                );

                paidPensions.add(paidPensionDto);
            } else if (!paid && !isPaid) {
                totalDebt += pension.getAmount();
                String status = pension.getDueDate().isBefore(currentDate) ? "Vencido" : "Pendiente";
                String pensionName = pensionNames[i];

                PensionDTO pensionDTO = new PensionDTO(
                        pension.getPensionCod(),
                        pensionName,
                        pension.getAmount(),
                        pension.getDueDate(),
                        status
                );
                pensions.add(pensionDTO);
            }
        }

        return ResponseEntity.ok().body(new ResponseFormat(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                new StudentPensionDTO(
                        paid ? 0 : totalDebt,
                        paid ? Collections.singletonList(paidPensions) : Collections.singletonList(pensions)
                )
        ));
    }

    private String getReceiptCodeFromPension(Pension pension){
        ReceiptPension receiptPension = receiptPensionRepository.findByPension(pension);
        return receiptPension.getReceipt().getReceiptCod();
    }

    private record PensionDTO(
            int pensionCod,
            String pensionName,
            Double pensionAmount,
            LocalDate dueDate,
            String status
    ){ }

    private record PaidPensionDto(
            int pensionCod,
            String pensionName,
            Double pensionAmount,
            LocalDate dueDate,
            String receiptId
    ){ }

    /*
        Functions for studentInformation
    */
    /**
     * Método interno usado para realizar los calculos del nuevo grado y nivel
     * a los que pasaría un estudiante en su siguente año de matrícula.
     *
     * @param currentGrade Grado actual del estudiante
     * @param currentLevel Nivel actual del estudiante
     * @return ResponseEntity con un objeto personalizado para la respuesta de tipo ResponseFormat.
     */
    public static String[] calculateNewLevelAndGrade(char currentGrade, String currentLevel) {

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
