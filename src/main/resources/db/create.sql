DROP DATABASE IF EXISTS alphaSolutions;
CREATE DATABASE alphaSolutions;
USE alphaSolutions;

-- Users table with ENUM-based roles

CREATE TABLE Users (
                       user_id INT PRIMARY KEY AUTO_INCREMENT,
                       user_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       role ENUM('employee', 'project manager', 'admin') NOT NULL,
                       password VARCHAR(255) NOT NULL
);

-- Projects table

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

-- Users and Projects junction

CREATE TABLE users_projects (
                                user_id INT,
                                project_id INT,
                                PRIMARY KEY (user_id, project_id),
                                FOREIGN KEY (user_id) REFERENCES Users(user_id),
                                FOREIGN KEY (project_id) REFERENCES Projects(project_id)
);

-- SubProjects table

CREATE TABLE SubProjects (
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

-- Tasks table

CREATE TABLE Tasks (
                       task_id INT PRIMARY KEY AUTO_INCREMENT,
                       sub_id INT,
                       task_name VARCHAR(100) NOT NULL,
                       task_desc TEXT,
                       task_deadline DATE NOT NULL,
                       task_timeEstimate INT NOT NULL,
                       task_dedicatedHours INT DEFAULT 0,
                       task_status BOOLEAN,
                       FOREIGN KEY (sub_id) REFERENCES SubProjects(sub_id) ON DELETE CASCADE
);

-- Labels

CREATE TABLE Labels (
                        label_id INT PRIMARY KEY AUTO_INCREMENT,
                        label_name VARCHAR(100)
);

-- Tasks and Labels junction

CREATE TABLE tasks_labels (
                              label_id INT,
                              task_id INT,
                              PRIMARY KEY (label_id, task_id),
                              FOREIGN KEY (label_id) REFERENCES Labels(label_id) ON DELETE CASCADE,
                              FOREIGN KEY (task_id) REFERENCES Tasks(task_id) ON DELETE CASCADE
);

-- Skills

CREATE TABLE Skills (
                        skill_id INT PRIMARY KEY AUTO_INCREMENT,
                        skill_name VARCHAR(100)
);

-- Users and Skills junction

CREATE TABLE users_skills (
                              user_id INT,
                              skill_id INT,
                              PRIMARY KEY(user_id, skill_id),
                              FOREIGN KEY (user_id) REFERENCES Users(user_id),
                              FOREIGN KEY (skill_id) REFERENCES Skills(skill_id)
);

-- Users and Tasks junction

CREATE TABLE users_tasks (
                             user_id INT,
                             task_id INT,
                             PRIMARY KEY(user_id, task_id),
                             FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
                             FOREIGN KEY (task_id) REFERENCES Tasks(task_id) ON DELETE CASCADE
);
