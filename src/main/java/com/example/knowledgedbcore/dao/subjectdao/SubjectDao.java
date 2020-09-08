package com.example.knowledgedbcore.dao.subjectdao;

import com.example.knowledgedbcore.models.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectDao {

    Optional<Subject> findByName(String name);

    Optional<List<Subject>> findByUsers_Id(Long users_id);
}
