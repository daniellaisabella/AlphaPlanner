package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.Project;
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
import java.util.stream.Collectors;

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
    private SubProject subProject2;

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
        subProject2 = new SubProject();
        subProject2.setSubProjectName("Test Sub2");
        subProject2.setSubProjectDesc("En testbeskrivelse2");
        subProject2.setSubProjectDeadline(LocalDate.of(2025, 11, 30));
        subProject2.setSubProjectStatus(false);
        subProject2.setSubDedicatedHours(5);
        subProject2.setSubEstimatedHours(10.0);
        subProject2.setProjectId(1);
    }


    //Test om metoden tilføjer et subprojekt til databsen
    @Test
    void testAddSubProjectToSql() {

        // indsæt instans i databasen
        subProjectRepository.addSubProjectToSql(subProject);
        subProjectRepository.addSubProjectToSql(subProject2);

        // Tjek at det faktisk blev tilføjet
        List<SubProject> result = jdbcTemplate.query("SELECT * FROM subprojects", new SubProjectRowMapper()); //jdbc laver sql query, der henter alle rækker  fra subpåroejcts. de bliver konvertert til en liste vha. rowmapper som oversætter databaserækker til instanser

        assertEquals(2, result.size()); //tjekker at den liste indeholder 1 instans

        SubProject saved = result.get(0); //henter subprojekt fra listen plads 0 og gemmer i variablen saved
        assertEquals("Test Sub", saved.getSubProjectName()); //sikrer følgende attributter er gemt med de ønskede værdier
        assertEquals("En testbeskrivelse", saved.getSubProjectDesc());
        assertEquals(LocalDate.of(2025, 11, 30), saved.getSubProjectDeadline());
        assertFalse(saved.getSubProjectStatus());
        assertEquals(10.5, saved.getSubDedicatedHours());
        assertEquals(15.0, saved.getSubEstimatedHours());
        assertEquals(1, saved.getProjectId());
        SubProject saved2 = result.get(1); //henter subprojekt fra listen plads 0 og gemmer i variablen saved
        assertEquals("Test Sub2", saved2.getSubProjectName()); //sikrer følgende attributter er gemt med de ønskede værdier
        assertEquals("En testbeskrivelse2", saved2.getSubProjectDesc());
        assertEquals(LocalDate.of(2025, 11, 30), saved2.getSubProjectDeadline());
        assertFalse(saved2.getSubProjectStatus());
        assertEquals(5, saved2.getSubDedicatedHours());
        assertEquals(10, saved2.getSubEstimatedHours());
        assertEquals(1, saved2.getProjectId());

    }

    @Test
    void deleteProjectSQL() {
        // indsæt instans i databsen
        subProjectRepository.addSubProjectToSql(subProject);
        subProjectRepository.addSubProjectToSql(subProject2);

        //slet fra datanbasen
        subProjectRepository.deleteProjectSQL(subProject2.getSubId());
        // Tjek at det faktisk blev tilføjet
        List<SubProject> result = jdbcTemplate.query("SELECT * FROM subprojects", new SubProjectRowMapper()); //jdbc laver sql query, der henter alle rækker  fra subpåroejcts. de bliver konvertert til en liste vha. rowmapper som oversætter databaserækker til instanser

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
    void updateSQL() {
        subProjectRepository.addSubProjectToSql(subProject);//insert subPÅroject to h2 in memory databse
        subProject.setSubProjectName("Updated name");// update name af status to the test
        subProject.setSubProjectStatus(true);
        subProjectRepository.updateSQL(subProject);//then run updateSQL method

        SubProject updatedProject = subProjectRepository.getSubProject(subProject.getSubId()); //get he instance from databse based on the id afdter update

        assertEquals("Updated name", updatedProject.getSubProjectName()); //JUnit assert, testing if the expected values equals the actual values
        assertTrue(updatedProject.getSubProjectStatus());

    }

    @Test
    void getSubProject() {
        subProjectRepository.addSubProjectToSql(subProject);
        SubProject testSub = subProjectRepository.getSubProject(subProject.getSubId());

        assertEquals("Test Sub", testSub.getSubProjectName());
        assertEquals("En testbeskrivelse", testSub.getSubProjectDesc());
        assertEquals(LocalDate.of(2025, 11, 30), testSub.getSubProjectDeadline());
        assertFalse(testSub.getSubProjectStatus());
        assertEquals(10.5, testSub.getSubDedicatedHours());
        assertEquals(15.0, testSub.getSubEstimatedHours());
        assertEquals(1, testSub.getProjectId());

    }

    @Test
    void getSubProjectAttachedToProject() {
        subProjectRepository.addSubProjectToSql(subProject);
        subProjectRepository.addSubProjectToSql(subProject2);

        List<SubProject> subProjects = subProjectRepository.getSubProjectAttachedToProject(1); // Act - run method

        //Assert
        assertNotNull(subProjects);
        assertEquals(2,subProjects.size());
        List<String> names = subProjects.stream() //make a String list from the objects names
                .map(SubProject::getSubProjectName)
                .collect(Collectors.toList()); //collects all names in a String List
        assertTrue(names.contains(subProject.getSubProjectName()));
        assertTrue(names.contains(subProject2.getSubProjectName()));



    }

    @Test
    void getSumDedicatedHours() {

        //Arrange
        subProjectRepository.addSubProjectToSql(subProject);
        subProjectRepository.addSubProjectToSql(subProject2);


        //Act
        double sum = subProjectRepository.getSumDedicatedHours(1);

        //ASSERT
        assertEquals(15.5,sum);
    }

    @Test
    void getSumEstimatedHours() {
        //Arrange
        subProjectRepository.addSubProjectToSql(subProject);
        subProjectRepository.addSubProjectToSql(subProject2);


        //Act
        double sum = subProjectRepository.getSumEstimatedHours(1);

        //ASSERT
        assertEquals(25,sum);


    }
}