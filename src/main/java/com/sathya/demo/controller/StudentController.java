package com.sathya.demo.controller;

import com.sathya.demo.entity.Student;
import com.sathya.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // List all students
    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "students"; // Renders students.html
    }
    
    @GetMapping("/")
    public String showHomePage() {
        return "index"; // Renders main.html
    }
    
    @GetMapping("/register")
    public String register() {
        return "second"; 
    }
   

    // Show form to create a new student
    @GetMapping("/new")
    public String createStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "create_student"; // Renders create_student.html
    }

    // Save a new student
    @PostMapping
    public String saveStudent(@ModelAttribute("student") Student student, RedirectAttributes redirectAttributes) {
        studentRepository.save(student);
        redirectAttributes.addFlashAttribute("message", "Student added successfully!");
        return "redirect:/students"; // Redirects to the student list
    }

    // Show form to edit an existing student
    @GetMapping("/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            redirectAttributes.addFlashAttribute("error", "Student with ID " + id + " not found.");
            return "redirect:/students"; // Redirect if student not found
        }
        model.addAttribute("student", student);
        return "edit_student"; // Renders edit_student.html
    }

    // Update an existing student
    @PostMapping("/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute("student") Student student, RedirectAttributes redirectAttributes) {
        Student existingStudent = studentRepository.findById(id).orElse(null);
        if (existingStudent != null) {
            existingStudent.setName(student.getName());
            existingStudent.setEmail(student.getEmail());
            existingStudent.setCourse(student.getCourse());
            studentRepository.save(existingStudent);
            redirectAttributes.addFlashAttribute("message", "Student updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Student with ID " + id + " not found.");
        }
        return "redirect:/students"; // Redirects to the student list
    }

    // Delete a student
    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Student deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Student with ID " + id + " not found.");
        }
        return "redirect:/students"; // Redirects to the student list
    }
}