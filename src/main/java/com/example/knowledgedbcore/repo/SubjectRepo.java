package com.example.knowledgedbcore.repo;

import com.example.knowledgedbcore.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepo extends JpaRepository<Subject, Integer> {

    Optional<Subject> findByName(String name);

    Optional<List<Subject>> findByUsers_Id(Long users_id);
}
