package com.integrador.svfapi.Service;

import com.integrador.svfapi.Repository.StudentRepository;
import com.integrador.svfapi.Classes.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


}