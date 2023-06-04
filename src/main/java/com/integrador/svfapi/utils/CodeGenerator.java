package com.integrador.svfapi.utils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDate;

@Component
@Data
@NoArgsConstructor
public class CodeGenerator {

    public String generateNextUserId(String lastUserId) {
        // Obtener el último ID registrado en la base de datos
        //String LastUserId = userRepository.findTopByOrderByUserIdDesc().getUserId();

        // Extraer el número de secuencia del último ID
        DecimalFormat df = new DecimalFormat("U000000");
        int number = Integer.parseInt(lastUserId.substring(1,7));

        // Generar el próximo número de secuencia y combinarlo con la parte fija del formato
        number++;
        return df.format(number);
    }

    public String generateNextStudentCod(String lastUserId) {
        // Obtener el último ID registrado en la base de datos
        //String LastUserId = studentRepository.findTopByOrderByStudentCodDesc().getStudentCod();

        // Extraer el número de secuencia del último ID
        DecimalFormat df = new DecimalFormat("SVF0000");
        int number = Integer.parseInt(lastUserId.substring(3,7));

        // Generar el próximo número de secuencia y combinarlo con la parte fija del formato
        number++;
        return df.format(number);
    }

    public String generateNextRelationCod(String lastRelationCod) {
        // Obtener el último ID registrado en la base de datos
        //String lastRelationCod = studentRepresentativesRepository.findTopByOrderByRelationCodeDesc().getRelationCode();

        // Extraer el número de secuencia del último ID
        DecimalFormat df = new DecimalFormat("R0000");
        int number = Integer.parseInt(lastRelationCod.substring(1,5));

        // Generar el próximo número de secuencia y combinarlo con la parte fija del formato
        number++;
        return df.format(number);
    }

    public String createDefaultPasswordFormat(String names, String dni) {
        int currentYear = LocalDate.now().getYear();
        String[] nameParts= names.split(" ");
        String firstName= nameParts[0];
        return String.format("%d%s%s", currentYear, firstName.toLowerCase(), dni);
    }

}
