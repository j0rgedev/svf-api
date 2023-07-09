package com.integrador.svfapi.service.impl;

import com.integrador.svfapi.classes.Pension;
import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.dto.dashboardDTO.*;
import com.integrador.svfapi.exception.BusinessException;
import com.integrador.svfapi.repository.PensionRepository;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.service.StatisticsService;
import com.integrador.svfapi.utils.CodeValidator;
import com.integrador.svfapi.utils.JwtUtil;
import com.integrador.svfapi.utils.TokenType;
import com.integrador.svfapi.utils.TokenValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final StudentRepository studentRepository;
    private final PensionRepository pensionRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public StatisticsServiceImpl(StudentRepository studentRepository, PensionRepository pensionRepository, JwtUtil jwtUtil) {
        this.studentRepository = studentRepository;
        this.pensionRepository = pensionRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<ResponseFormat> getGeneralStatistics(String token, int monthNumber) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.ADMIN)) {
            List<MonthPensionsCount> monthPensionsCount = getPensionQuantity(monthNumber);

            List<Student> lastFiveStudent;
            if (monthNumber == 0) {
                lastFiveStudent = studentRepository.getLastFiveEnrolledStudents();
            } else {
                lastFiveStudent = studentRepository.getLastFiveEnrolledStudentsByMonth(monthNumber);
            }
            List<LastEnrolledStudentsDTO> lastEnrolledStudentsDTO = getLastFiveStudentsDTO(lastFiveStudent);

            List<Student> studentList;
            if (monthNumber == 0) {
                studentList = studentRepository.findActiveStudents();
            } else {
                studentList = studentRepository.findActiveStudentsByMonth(monthNumber);
            }

            EnrollmentCountDTO enrollmentCountDTO = getEnrollmentCountDTO(studentList, monthNumber);

            return ResponseEntity.ok().body(new ResponseFormat(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    new GeneralStatistics(
                            monthPensionsCount,
                            lastEnrolledStudentsDTO,
                            enrollmentCountDTO
                    )
            ));
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Token invalido");
        }
    }

    private List<MonthPensionsCount> getPensionQuantity(int month) {
        List<MonthPensionsCount> monthPensionsCounts = new ArrayList<>();

        if (month == 0) {
            List<Object[]> pensionsAmount = pensionRepository.getPensionsQuantity();

            for (Object[] pensionAmount : pensionsAmount) {
                monthPensionsCounts.add(new MonthPensionsCount(
                        (int) pensionAmount[0],
                        (Long) pensionAmount[1]
                ));
            }
        } else {
            List<Object[]> pensionsAmountByMonth = pensionRepository.getPensionsQuantityByMonth(month);
            for (Object[] pensionAmount : pensionsAmountByMonth) {
                monthPensionsCounts.add(new MonthPensionsCount(
                        (int) pensionAmount[0],
                        (Long) pensionAmount[1]
                ));
            }
        }
        return monthPensionsCounts;
    }

    private List<LastEnrolledStudentsDTO> getLastFiveStudentsDTO(List<Student> lastFiveStudent) {
        List<LastEnrolledStudentsDTO> lastEnrolledStudentsDTO = new ArrayList<>();
        for (Student student : lastFiveStudent) {
            lastEnrolledStudentsDTO.add(new LastEnrolledStudentsDTO(
                    student.getStudentCod(),
                    student.getNames() + " " + student.getLastNames(),
                    student.getCurrentLevel()));
        }
        return lastEnrolledStudentsDTO;
    }

    private EnrollmentCountDTO getEnrollmentCountDTO(List<Student> studentList, int monthNumber) {
        int totalStudents, enrolled, notEnrolled;
        if (monthNumber == 0) {
            totalStudents = studentList.size();
            enrolled = (int) studentList.stream().filter(Student::isEnrolled).count();
            notEnrolled = totalStudents - enrolled;
        } else {
            totalStudents = 0;
            enrolled = studentList.size();
            notEnrolled = 0;
        }
        return new EnrollmentCountDTO(totalStudents, enrolled, notEnrolled);
    }

    @Override
    public ResponseEntity<ResponseFormat> getEnrollmentStatistics(String token) {

        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.ADMIN)) {
//        // Conteo de alumnos por genero
            List<Student> studentList = studentRepository.findActiveStudents();
            List<Student> enrolledStudentList = studentList.stream().filter(Student::isEnrolled).toList();
            int totalStudents = enrolledStudentList.size();
            int boys = (int) enrolledStudentList.stream().filter(student -> student.getGender() == 'M').count();
            int girls = totalStudents - boys;
            EnrolledByGenderDTO enrolledByGenderDTO = new EnrolledByGenderDTO(boys, girls, totalStudents);

            // Conteo de matriculas por año
            List<EnrollmentCountByYearAndLevel> enrollmentCountByYearAndLevelList = studentRepository.getEnrollmentCountByYearAndLevel();
            Map<Integer, List<LevelCount>> enrollmentByYear = new HashMap<>();
            for (EnrollmentCountByYearAndLevel countByYearAndLevel : enrollmentCountByYearAndLevelList) {
                int year = countByYearAndLevel.getYear();
                Optional<List<LevelCount>> result = Optional.ofNullable(enrollmentByYear.get(year));
                if (result.isPresent()) {
                    List<LevelCount> levelCounts = result.get();
                    levelCounts.add(new LevelCount(countByYearAndLevel.getCurrentLevel(), (int) countByYearAndLevel.getCount()));
                    enrollmentByYear.put(year, levelCounts);
                } else {
                    List<LevelCount> levelCounts = new ArrayList<>();
                    levelCounts.add(new LevelCount(countByYearAndLevel.getCurrentLevel(), (int) countByYearAndLevel.getCount()));
                    enrollmentByYear.put(year, levelCounts);
                }
            }
//
            // Conteo de matriculas por nivel y grado
            List<Student> enrolledStudents = studentRepository.findByIsEnrolled(true);

            int[] inicialCounts = new int[3];
            String firstLevel = "Primaria";

            for (int i = 0; i < inicialCounts.length; i++) {
                char grade = Character.forDigit(i + 1, 10);
                inicialCounts[i] = countByLevelAndGrade(enrolledStudents, firstLevel, grade);
            }

            int[] primariaCounts = new int[6];
            String secondLevel = "Primaria";

            for (int i = 0; i < primariaCounts.length; i++) {
                char grade = Character.forDigit(i + 1, 10);
                primariaCounts[i] = countByLevelAndGrade(enrolledStudents, secondLevel, grade);
            }

            int[] secundariaCounts = new int[5];
            String thirdLevel = "Secundaria";

            for (int i = 0; i < secundariaCounts.length; i++) {
                char grade = Character.forDigit(i + 1, 10);
                secundariaCounts[i] = countByLevelAndGrade(enrolledStudents, thirdLevel, grade);
            }

            Map<String, Map<String, Integer>> enrollmentByLevelAndGrade = new HashMap<>();

            // Crear los niveles y grados iniciales
            Map<String, Integer> inicialGrades = new HashMap<>();
            // Agregar los grados de Inicial
            inicialGrades.put("3", inicialCounts[0]);
            inicialGrades.put("4", inicialCounts[1]);
            inicialGrades.put("5", inicialCounts[2]);

            enrollmentByLevelAndGrade.put("Inicial", inicialGrades);

            Map<String, Integer> primariaGrades = new HashMap<>();
            // Agregar los grados de Primaria
            primariaGrades.put("1", primariaCounts[0]);
            primariaGrades.put("2", primariaCounts[1]);
            primariaGrades.put("3", primariaCounts[2]);
            primariaGrades.put("4", primariaCounts[3]);
            primariaGrades.put("5", primariaCounts[4]);
            primariaGrades.put("6", primariaCounts[5]);

            enrollmentByLevelAndGrade.put("Primaria", primariaGrades);

            Map<String, Integer> secundariaGrades = new HashMap<>();
            // Agregar los grados de Secundaria
            secundariaGrades.put("1", secundariaCounts[0]);
            secundariaGrades.put("2", secundariaCounts[1]);
            secundariaGrades.put("3", secundariaCounts[2]);
            secundariaGrades.put("4", secundariaCounts[3]);
            secundariaGrades.put("5", secundariaCounts[4]);

            enrollmentByLevelAndGrade.put("Secundaria", secundariaGrades);

            // Creación de la data
            Map<String, Object> data = new HashMap<>();
            data.put("enrolledStudents", enrolledByGenderDTO);
            data.put("enrollmentByYear", enrollmentByYear);
            data.put("enrollmentByLevelAndGrade", enrollmentByLevelAndGrade);

            //TODO: Crear una función para cada uno de los reportes

            return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), "OK", data));
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "No autorizado");
        }
    }

    private int countByLevelAndGrade(List<Student> studentList, String level, char grade) {
        return (int) studentList.stream()
                .filter(student -> student.getCurrentLevel().equals(level) && student.getCurrentGrade() == grade)
                .count();
    }

    @Override
    public ResponseEntity<ResponseFormat> getPensionStatistics(String token) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.ADMIN)) {
            List<Pension> pensionList = pensionRepository.findAll();
            return ResponseEntity.ok().body(new ResponseFormat(
                            HttpStatus.OK.value(),
                            HttpStatus.OK.getReasonPhrase(),
                            new PensionStatistics(
                                    getMonthPensionsAmount(pensionList),
                                    getStudentsPaymentStatus(pensionList)
                            )
                    )
            );
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "No autorizado");
        }
    }

    private List<MonthPensionsAmount> getMonthPensionsAmount(List<Pension> pensionList) {
        List<MonthPensionsAmount> monthPensionsAmountList = new ArrayList<>();
        Map<String, Double> monthAmount = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM", new Locale("es"));
        Month startMonth = Month.MARCH;

        for (Pension pension : pensionList) {
            LocalDate dueDate = pension.getDueDate();
            if (dueDate.getMonth().getValue() >= startMonth.getValue()) {
                String formattedMonth = formatter.format(dueDate);
                if (pension.isStatus()) {
                    double amount = pension.getAmount();
                    monthAmount.put(formattedMonth, monthAmount.getOrDefault(formattedMonth, 0.0) + amount);
                }
            }
        }

        for (Month month : Month.values()) {
            if (month.getValue() == 1 || month.getValue() == 2) {
                continue;
            }
            String monthName = month.getDisplayName(TextStyle.SHORT, new Locale("es"));
            double amount = monthAmount.getOrDefault(monthName, 0.0);
            monthPensionsAmountList.add(new MonthPensionsAmount(monthName, amount));
        }

        return monthPensionsAmountList;
    }

    private StudentsPaymentStatus getStudentsPaymentStatus(List<Pension> pensionList) {
        Set<String> paidStudents = new HashSet<>();
        Set<String> unpaidStudents = new HashSet<>();

        for (Pension pension : pensionList) {
            String studentId = pension.getStudent().getStudentCod();
            LocalDate currentDate = LocalDate.now();
            LocalDate dueDate = pension.getDueDate();

            if (pension.isStatus() && currentDate.isAfter(dueDate)) {
                paidStudents.add(studentId);
            } else {
                unpaidStudents.add(studentId);
            }
        }

        return new StudentsPaymentStatus(paidStudents.size(), unpaidStudents.size());
    }

    @Override
    public ResponseEntity<ResponseFormat> getTotalDebt(String token, int monthNumber) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.ADMIN)) {
            List<Double[]> totalDebt = pensionRepository.getDebtByMonth(monthNumber);

            return ResponseEntity.ok().body(new ResponseFormat(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    new DebtByMonth(
                            totalDebt.get(0)[0],
                            totalDebt.get(0)[1],
                            totalDebt.get(0)[2]
                    )
            ));
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "No autorizado");
        }
    }


}
