package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.repository.rowmappers.SubProjectRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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


    private SubProject subProject;

    @BeforeEach
    void setUpdatabase() {
        // starter databasen frisk hver gang - ryd subproject tabellen
        jdbcTemplate.update("DELETE FROM tasks_labels");
        jdbcTemplate.update("DELETE FROM users_tasks");
        jdbcTemplate.update("DELETE FROM tasks");
        jdbcTemplate.update("DELETE FROM subprojects");
        jdbcTemplate.update("DELETE FROM users_projects");


        //Indsæt et projekt så databsen har et id at tilknytte subprojekt

        jdbcTemplate.update("""
                    INSERT INTO projects(project_name, project_desc, project_deadline, project_status, project_dedicatedHours, project_timeEstimate)
                    VALUES (?, ?, ?, ?, ?, ?)
                """, "Testprojekt", "Testbeskrivelse", LocalDate.of(2025, 12, 31), false, 100, 120);


        //instans af subproject
       subProject = new SubProject();
        subProject.setSubProjectName("Test Sub");
        subProject.setSubProjectDesc("En testbeskrivelse");
        subProject.setSubProjectDeadline(LocalDate.of(2025, 11, 30));
        subProject.setSubProjectStatus(false);
        subProject.setSubDedicatedHours(10.5);
        subProject.setSubEstimatedHours(15.0);
        subProject.setProjectId(1);
    }


    //Test om metoden tilføjer et subprojekt til databsen
    @Test
    void testAddSubProjectToSql() {
        System.out.println("Test SubDedicatedHours før indsættelse: " + subProject.getSubDedicatedHours());
        // indsæt instans i databsen
        subProjectRepository.addSubProjectToSql(subProject);

        // Tjek at det faktisk blev tilføjet
        List<SubProject> result = jdbcTemplate.query("SELECT * FROM subprojects", new SubProjectRowMapper()); //jdbc laver sql query, der henter alle rækker  fra subpåroejcts. de bliver konvertert til en liste vha. rowmapper som oversætter databaserækker til instanser
        System.out.println("Database subDedicatedHours: " + result.get(0).getSubDedicatedHours());
        assertEquals(1, result.size()); //tjekker at den liste indeholder 1 instans

        SubProject saved = result.get(0); //henter subprojekt fra listen plads 0 og gemmer i variablen saved
        assertEquals("Test Sub", saved.getSubProjectName()); //sikrer følgende attributter er gemt med de ønskede værdier
        assertEquals("En testbeskrivelse", saved.getSubProjectDesc());
        assertEquals(LocalDate.of(2025, 11, 30), saved.getSubProjectDeadline());
        assertFalse(saved.getSubProjectStatus());
        assertEquals(10.5, saved.getSubDedicatedHours());
        assertEquals(15.0, saved.getSubEstimatedHours());
        assertEquals(1, saved.getProjectId());

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