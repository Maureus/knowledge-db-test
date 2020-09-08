package com.example.knowledgedbcore.dao.subjectdao;

import com.example.knowledgedbcore.models.Subject;
import com.example.knowledgedbcore.repo.SubjectRepo;

import java.util.List;
import java.util.Optional;

public class SubjectDaoImpl implements SubjectDao {

    private final SubjectRepo subjectRepo;

    public SubjectDaoImpl(SubjectRepo subjectRepo) {
        this.subjectRepo = subjectRepo;
    }

    @Override
    public Optional<Subject> findByName(String name) {
        return subjectRepo.findByName(name);
    }

    @Override
    public Optional<List<Subject>> findByUsers_Id(Long users_id) {
        return subjectRepo.findByUsers_Id(users_id);
    }
}
