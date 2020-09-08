package com.example.knowledgedbcore.controllers;

import com.example.knowledgedbcore.models.Subject;
import com.example.knowledgedbcore.models.UserSubjects;
import com.example.knowledgedbcore.repo.SubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Validated
public class SubjectController {

    private SubjectRepo subjectRepo;

    @Autowired
    public SubjectController(SubjectRepo subjectRepo) {
        this.subjectRepo = subjectRepo;
    }

    @GetMapping("/api/v1/subjects?_user={id}")
    public UserSubjects getSubjects(@PathVariable("id") Long id) {
        Optional<List<Subject>> subjects = subjectRepo.findByUsers_Id(id);
        subjects.orElseThrow(() -> new IllegalStateException("No subjects found"));
        UserSubjects userSubjects = new UserSubjects();
        userSubjects.setSubjectList(subjects.get());
        return userSubjects;
    }

    @GetMapping("/api/v1/subjects")
    public UserSubjects getAll() {
        UserSubjects subjects = new UserSubjects();
        subjects.setSubjectList(subjectRepo.findAll());
        return subjects;
    }

    @PostMapping("/api/v1/subjects")
    public Subject insert(@Valid @RequestBody Subject subject) {
        return subjectRepo.save(subject);
    }

}
