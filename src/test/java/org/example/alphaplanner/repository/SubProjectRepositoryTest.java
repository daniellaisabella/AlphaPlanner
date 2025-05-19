package org.example.alphaplanner.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static java.nio.file.attribute.AclEntryPermission.DELETE;
import static org.h2.util.ParserUtil.FROM;
import static org.junit.jupiter.api.Assertions.*;


//Starter appliaktionskonteksten
@SpringBootTest
//Før testemetode køres, eksekveres h2init.sql på in memory H2 datatbsae
@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)

@Transactional
@Rollback(true)//ændringer foretaget i databasen under testen rulles tilbage, når testen er færdig - hver test kører på en "frisk" databse

class SubProjectRepositoryTest {
    //Automatisk injektere afhængigheder (objekter/berans) - Spring sørger for man får de rigtige instanser
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SubProjectRepository subProjectRepository;

@BeforeEach
void setUpdatabase(){
    // starter databasen frisk hver gang - ryd subproject tabellen
    jdbcTemplate.execute("DELETE FROM subprojects");
    jdbcTemplate.execute("DELETE FROM projects");

    //Indsæt et projekt
    jdbcTemplate.update("""
            INSERT INTO projects(project_id, project_name, project_description, project_deadline, project_status)
            VALUES (?, ?, ?, ?, ?)
        """, 1, "Testprojekt", "Testbeskrivelse", LocalDate.of(2025, 12, 31), false);
}
    //Test om metoden tilføjer et subprojekt til databsen
    @Test
    void testAddSubProjectToSql() {

    }

    @Test
    void deleteProjectSQL() {
    }

    @Test
    void updateSQL() {
    }

    @Test
    void getSubProject() {
    }

    @Test
    void getSubProjectAttachedToProject() {
    }

    @Test
    void getSumDedicatedHours() {
    }

    @Test
    void getSumEstimatedHours() {
    }
}