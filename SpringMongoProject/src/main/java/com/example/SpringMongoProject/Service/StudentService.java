package com.example.SpringMongoProject.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.SpringMongoProject.Model.DatabaseSequence;
import com.example.SpringMongoProject.Model.Student;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import com.example.SpringMongoProject.Repository.StudentRepo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;

import org.springframework.data.mongodb.core.query.Update;


import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private MongoOperations mongoOperations;

    public List<Student> getAllStudents() {
        logger.info("Fetching all students");
        return studentRepo.findAll();
    }
    public long generateSequence(String seqName) {
    DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
      new Update().inc("seq",1), options().returnNew(true).upsert(true),
      DatabaseSequence.class);
    return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }

    public void deleteStudent(String id) {
        if (studentRepo.existsById(id)) {
            logger.info("Deleting student with ID: {}", id);
            studentRepo.deleteById(id);
        } else {
            logger.warn("Student with ID: {} not found for deletion", id);
        }
    }

    public Optional<Student> getStudentById(String id) {
        logger.info("Fetching student with ID: {}", id);
        return studentRepo.findById(id);
    }

    public void saveStudent(Student student) {
     Student studentTemp = new Student(); 
     studentTemp.setStudentId(String.valueOf(generateSequence(Student.SEQUENCE_NAME)));
        studentTemp.setStudentName(student.getStudentName());
        studentTemp.setStudentAddress(student.getStudentAddress());
        studentTemp.setMobile(student.getMobile());
        studentTemp.setAge(student.getAge());
        studentTemp.setGPA(student.getGPA());
        logger.info("Saving student: {}", studentTemp);
        studentRepo.save(studentTemp);

    }

    public Student partialUpdateStudent(String id, Student updatedStudent) {
        Optional<Student> optionalExistingStudent = studentRepo.findById(id);
    
        if (optionalExistingStudent.isPresent()) {
            Student existingStudent = optionalExistingStudent.get();
    
            // Update only non-null fields
            if (updatedStudent.getStudentName() != null) {
                existingStudent.setStudentName(updatedStudent.getStudentName());
            }
            if (updatedStudent.getStudentAddress() != null) {
                existingStudent.setStudentAddress(updatedStudent.getStudentAddress());
            }
            if (updatedStudent.getMobile() != null) {
                existingStudent.setMobile(updatedStudent.getMobile());
            }
            if (updatedStudent.getAge() != 0) {
                existingStudent.setAge(updatedStudent.getAge());
            }
            if (updatedStudent.getGPA() != 0) {
                existingStudent.setGPA(updatedStudent.getGPA());
            }
    
            // Save the updated student
            studentRepo.save(existingStudent);
            logger.info("Student with ID: {} updated successfully", id);
            return existingStudent;
        } else {
            logger.warn("Student with ID: {} not found for update", id);
            throw new RuntimeException("Student with ID " + id + " not found");
        }
    }

    public List<Student> getStudentsWithPagination(int pageNo, int pageSize) {
        logger.info("Fetching students with pagination: pageNo={}, pageSize={}", pageNo, pageSize);
        int skip = (pageNo - 1) * pageSize;
        return studentRepo.findAll().stream()
                .skip(skip)
                .limit(pageSize)
                .toList();
    }

  public List<Student> getStudentInfoPaginated(int pageNumber, int pageSize,String[] sortBy, HttpServletResponse response) {
        
        // Sort sort = Sort.by(sortBy[0]);
        // for(int i = 1; i<sortBy.length;i++){
            
        //    sort.and (Sort.by(sortBy[i]));
        // }
        Pageable page = PageRequest.of(pageNumber, pageSize,Sort.by(sortBy));
        Page<Student> studentPage = studentRepo.findAll(page);
        response.addHeader("totalStudentPage",""+studentPage.getTotalPages());
         List<Student> studentList = studentPage.getContent();
        return studentList;
    }

}
