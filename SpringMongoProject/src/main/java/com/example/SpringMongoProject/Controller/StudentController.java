package com.example.SpringMongoProject.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringMongoProject.Model.Student;
import com.example.SpringMongoProject.Service.StudentService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;





@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/students")


public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("")
    private String saveStudent (@Valid @RequestBody Student student) {
        studentService.saveStudent(student);
        return "Student saved successfully!";
    }
    @GetMapping("/getAll")
    private List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
    @PostMapping("/edit/{id}")
    public Student editStudent(@RequestBody Student student, @PathVariable String id) {
        return studentService.partialUpdateStudent(id, student);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return id;
    }
    @GetMapping("/student/{id}")
    private Student getStudentById(@PathVariable String id) {
        return studentService.getStudentById(id).orElse(null);
    }
    @PostMapping("/saveAll")
    private String saveAllStudents(@Valid @RequestBody List<Student> students) {
        for (Student student : students) {
            studentService.saveStudent(student);
        }
        return "All students saved successfully!";
    }
   @GetMapping("/pagination/pageNumber/{pageNumber}/size/{pageSize}")
    public List<Student> getStudentInfoPaginated(
        @PathVariable int pageNumber, 
        @PathVariable int pageSize,
        HttpServletResponse response,
     @RequestParam(name="sortBy",defaultValue = "id")String... sortBy) {
        return studentService.getStudentInfoPaginated(pageNumber, pageSize,sortBy, response);
    }

    
}
    

    
   
  
    
    

