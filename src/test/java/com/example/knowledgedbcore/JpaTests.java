package com.example.knowledgedbcore;

import com.example.knowledgedbcore.models.Role;
import com.example.knowledgedbcore.models.Subject;
import com.example.knowledgedbcore.models.User;
import com.example.knowledgedbcore.repo.RoleRepo;
import com.example.knowledgedbcore.repo.SubjectRepo;
import com.example.knowledgedbcore.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.knowledgedbcore.models.UserRole.ROLE_ADMIN;
import static com.example.knowledgedbcore.models.UserRole.ROLE_USER;


@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
public class JpaTests {

    @Autowired
    private SubjectRepo subjectRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    @Test
    public void testSaveToRepoAndFindById() {
        Role adminRole = roleRepo.save(new Role(ROLE_ADMIN));
        Role userRole = roleRepo.save(new Role(ROLE_USER));
        System.out.println(ANSI_GREEN + "" + adminRole + ", " + userRole + "" + ANSI_RESET);

        Set<Role> roles = Stream.of(adminRole, userRole)
                .collect(Collectors.toSet());
        User usr1 = new User("Andrew", "1234", "andrew@test.com", roles);

        Subject sbj1 = new Subject("Matematika 1", "1", "1", "5");
        Subject sbj2 = new Subject("Matematika 2", "2", "1", "5");

        subjectRepo.save(sbj1);
        subjectRepo.save(sbj2);

        usr1.getSubjects().add(sbj1);
        usr1.getSubjects().add(sbj2);


        userRepo.save(usr1);


        Optional<User> user = userRepo.findUserByUsername("Andrew");
        user.orElseThrow(() -> {
            throw new IllegalArgumentException("No data");
        });
        System.out.println(ANSI_GREEN + "" + user.get() + "" + ANSI_RESET);

        Optional<Subject> subject1 = subjectRepo.findByName("Matematika 1");
        subject1.orElseThrow(() -> {
            throw new IllegalArgumentException("No data");
        });

        System.out.println(ANSI_GREEN + "" + subject1.get() + "" + ANSI_RESET);

        Optional<Subject> subject2 = subjectRepo.findByName("Matematika 2");
        subject2.orElseThrow(() -> {
            throw new IllegalArgumentException("No data");
        });

        System.out.println(ANSI_GREEN + "" + subject2.get() + "" + ANSI_RESET);

        Optional<List<Subject>> subjects = subjectRepo.findByUsers_Id(user.get().getId());
        subjects.orElseThrow(() -> {
            throw new IllegalArgumentException("No data");
        });

        System.out.println(ANSI_GREEN + "" + subjects.get() + "" + ANSI_RESET);

    }

}
