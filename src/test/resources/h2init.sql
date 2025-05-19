-- Drop tables if they exist
DROP TABLE IF EXISTS users_tasks;
DROP TABLE IF EXISTS users_skills;
DROP TABLE IF EXISTS tasks_labels;
DROP TABLE IF EXISTS Labels;
DROP TABLE IF EXISTS Tasks;
DROP TABLE IF EXISTS SubProjects;
DROP TABLE IF EXISTS users_projects;
DROP TABLE IF EXISTS Projects;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Skills;

-- Users table with ENUM-based roles

CREATE TABLE Users (
                       user_id INT PRIMARY KEY AUTO_INCREMENT,
                       user_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       role ENUM('employee', 'project manager', 'admin') NOT NULL,
                       password VARCHAR(255)
);

-- Projects table

CREATE TABLE Projects (
                          project_id INT PRIMARY KEY AUTO_INCREMENT,
                          pm_id INT NULL,
                          project_name VARCHAR(100) NOT NULL,
                          project_desc VARCHAR(100) NOT NULL,
                          project_deadline DATE NOT NULL,
                          project_status BOOLEAN,
                          project_dedicatedHours DOUBLE NOT NULL,
                          project_timeEstimate DOUBLE NOT NULL,
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
                             sub_dedicatedHours DOUBLE,
                             sub_timeEstimate DOUBLE,
                             FOREIGN KEY (project_id) REFERENCES Projects(project_id) ON DELETE CASCADE
);

-- Tasks table

CREATE TABLE Tasks (
                       task_id INT PRIMARY KEY AUTO_INCREMENT,
                       sub_id INT,
                       task_name VARCHAR(100) NOT NULL,
                       task_desc VARCHAR(100),
                       task_deadline DATE NOT NULL,
                       task_timeEstimate DOUBLE NOT NULL,
                       task_dedicatedHours DOUBLE,
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
                              FOREIGN KEY (label_id) REFERENCES Labels(label_id),
                              FOREIGN KEY (task_id) REFERENCES Tasks(task_id)
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
                             FOREIGN KEY (user_id) REFERENCES Users(user_id),
                             FOREIGN KEY (task_id) REFERENCES Tasks(task_id)
);

-- Users
INSERT INTO Users (user_name, email, role, password) VALUES
                                                         ('Alice Johnson', 'alice@alpha.com', 'Admin', '123'),
                                                         ('Bob Smith', 'bob@alpha.com', 'Project Manager', '123'),
                                                         ('Charlie Lee', 'charlie@alpha.com', 'Employee', '123'),
                                                         ('Diana Kim', 'diana@alpha.com', 'Employee', '123');

-- Projects
INSERT INTO Projects (pm_id, project_name, project_desc, project_deadline, project_status, project_dedicatedHours, project_timeEstimate) VALUES
                                                                                                                                             (2, 'Website Redesign', 'Redesign company website', '2025-07-15', TRUE, 200, 250),
                                                                                                                                             (2, 'Mobile App Launch', 'Launch alpha mobile app', '2025-09-01', FALSE, 150, 180);

-- Users_Projects
INSERT INTO users_projects (user_id, project_id) VALUES
                                                     (2, 1), -- Bob on Project 1
                                                     (3, 1), -- Charlie on Project 1
                                                     (4, 2); -- Diana on Project 2

-- SubProjects
INSERT INTO SubProjects (project_id, sub_name, sub_desc, sub_deadline, sub_status, sub_dedicatedHours, sub_timeEstimate) VALUES
                                                                                                                             (1, 'UI Design', 'Design layout and branding', '2025-06-01', TRUE, 60, 70),
                                                                                                                             (1, 'Backend API', 'Develop backend API', '2025-07-01', FALSE, 90, 100),
                                                                                                                             (2, 'Beta Testing', 'Internal app testing', '2025-08-15', FALSE, 40, 50);

-- Tasks
INSERT INTO Tasks (sub_id, task_name, task_desc, task_deadline, task_timeEstimate, task_dedicatedHours, task_status) VALUES
                                                                                                                         (1, 'Create wireframes', 'Initial wireframes for UI', '2025-05-01', 10, 8, TRUE),
                                                                                                                         (1, 'Mockup screens', 'High fidelity mockups', '2025-05-15', 15, 10, FALSE),
                                                                                                                         (2, 'Setup database', 'Schema and migration', '2025-06-10', 20, 18, FALSE),
                                                                                                                         (3, 'Bug reports', 'Testers log bugs', '2025-08-01', 10, NULL, FALSE);

-- Labels
INSERT INTO Labels (label_name) VALUES
                                    ('UI'), ('Backend'), ('Testing'), ('Urgent');

-- tasks_labels
INSERT INTO tasks_labels (label_id, task_id) VALUES
                                                 (1, 1), -- UI label to wireframes
                                                 (1, 2), -- UI label to mockups
                                                 (2, 3), -- Backend label
                                                 (3, 4); -- Testing label

-- Skills
INSERT INTO Skills (skill_name) VALUES
                                    ('JavaScript'), ('SQL'), ('UI Design'), ('Testing');

-- users_skills
INSERT INTO users_skills (user_id, skill_id) VALUES
                                                 (1, 2), -- Alice knows SQL
                                                 (2, 1), -- Bob knows JavaScript
                                                 (3, 3), -- Charlie knows UI Design
                                                 (4, 4); -- Diana knows Testing

-- users_tasks
INSERT INTO users_tasks (user_id, task_id) VALUES
                                               (3, 1), -- Charlie on wireframes
                                               (3, 2), -- Charlie on mockups
                                               (2, 3), -- Bob on database
                                               (4, 4); -- Diana on testing