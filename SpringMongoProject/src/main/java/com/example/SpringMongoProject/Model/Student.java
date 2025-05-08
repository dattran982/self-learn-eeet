package com.example.SpringMongoProject.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "students")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Transient
    public static final String SEQUENCE_NAME = "students_sequence";

    @Id
   
   
    private String studentId;
    
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, message = "Name should have at least 2 characters")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Name should only contain letters and spaces")
    private String studentName; 

    @NotBlank(message = "Address cannot be blank")
    @Size(min = 5, message = "Address should have at least 5 characters")
    private String studentAddress;

    @NotNull
    @Size(min = 10, max = 15, message = "Mobile number should be between 10 and 15 digits")
    private String mobile; 
    @NotNull
    @Min(value = 1, message = "Age should be at least 1")
    @Max(value = 100, message = "Age should be less than or equal to 100")
    private int age;
    @NotNull
    @Min(value = 0, message = "GPA should be at least 0")
    @Max(value = 4, message = "GPA should be less than or equal to 4")
    private Double GPA;
}
