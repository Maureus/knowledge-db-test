package com.example.knowledgedbcore;

import com.example.knowledgedbcore.models.Subject;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = KnowledgeDbApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SubjectControllerTest {
    @LocalServerPort
    private int port;

    @Test
    public void testPostSubject() {
        Subject subject = new Subject(1, "Matematika", "1", "1", "1");

        when()
                .post("http://localhost:" + port + "/api/v1/subjects", subject)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testGetAll() {
        when()
                .get("http://localhost:" + port + "/api/v1/subjects")
//                .as(UserSubjects.class, )
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

}
