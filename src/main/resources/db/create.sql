
CREATE DATABASE alphaSolutions;

USE alphaSolutions;

-- Creates a table for users

CREATE TABLE Users (
                       user_id INT PRIMARY KEY AUTO_INCREMENT,
                       roll_id INT,
                       user_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       role ENUM('employee', 'project manager', 'admin') NOT NULL
    password VARCHAR(255) NOT NULL
);


-- Creates a table for projects

CREATE TABLE Projects (
                          project_id INT PRIMARY KEY AUTO_INCREMENT,
                          pm_id INT NULL,
                          project_name VARCHAR(100) NOT NULL,
                          project_desc VARCHAR(100) NOT NULL,
                          project_deadline DATE NOT NULL,
                          project_status BOOLEAN,
                          project_dedicatedHours INT NOT NULL,
                          project_timeEstimate INT NOT NULL,
                          FOREIGN KEY (pm_id) REFERENCES Users(user_id) ON DELETE SET NULL

);

-- Junction table for a many-to-many relationship between users and projects

CREATE TABLE users_projects (
                                user_id INT,
                                project_id INT,
                                PRIMARY KEY (user_id, project_id),
                                FOREIGN KEY (user_id) REFERENCES Users(user_id),
                                FOREIGN KEY (project_id) REFERENCES Projects(project_id)
);

-- table for subprojects. One to many rel with projects.

CREATE TABLE SubProjects(
                            sub_id INT PRIMARY KEY AUTO_INCREMENT,
                            project_id INT,
                            sub_name VARCHAR(100) NOT NULL,
                            sub_desc VARCHAR(100),
                            sub_deadline DATE NOT NULL,
                            sub_status BOOLEAN,
                            sub_dedicatedHours INT,
                            sub_timeEstimate INT,
                            FOREIGN KEY (project_id) REFERENCES Projects(project_id) ON DELETE CASCADE

);

CREATE TABLE Tasks(
                      task_id INT PRIMARY KEY AUTO_INCREMENT,
                      sub_id INT,
                      task_name VARCHAR(100) NOT NULL,
                      task_desc VARCHAR(100),
                      task_deadline DATE NOT NULL,
                      task_timeEstimate INT NOT NULL,
                      task_dedicatedHours INT,
                      task_status BOOLEAN,
                      FOREIGN KEY (sub_id) REFERENCES SubProjects(sub_id) ON DELETE CASCADE
);

--Table for labels

CREATE TABLE Labels(
                       label_id INT PRIMARY KEY AUTO_INCREMENT,
                       label_name VARCHAR(100)
);

-- Junction table between tasks and labels

CREATE TABLE tasks_labels(
                             label_id INT,
                             task_id INT,
                             PRIMARY KEY (label_id, task_id),
                             FOREIGN KEY (label_id) REFERENCES Labels(label_id),
                             FOREIGN KEY (task_id) REFERENCES Tasks(task_id)
);

-- table for skills. many to many with users

CREATE TABLE Skills(
                       skill_id INT PRIMARY KEY AUTO_INCREMENT,
                       skill_name VARCHAR(100)
);

-- junction table between users and skills

CREATE TABLE users_skills(
                             user_id INT,
                             skill_id INT,
                             PRIMARY KEY(user_id, skill_id),
                             FOREIGN KEY (user_id) REFERENCES Users(user_id),
                             FOREIGN KEY (skill_id) REFERENCES Skills(skill_id)
);

-- Junction table between users and taks

CREATE TALBE users_tasks(
user_id INT,
task_id INT,
PRIMARY KEY(user_id, task_id),
FOREIGN KEY (user_id) REFERENCES Users(user_id),
FOREIGN KEY (task_id) REFERENCES Tasks(task_id)
);






