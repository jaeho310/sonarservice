DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS maven_files;
DROP TABLE IF EXISTS sonarqube;

CREATE TABLE user
(
    id             INT            NOT NULL AUTO_INCREMENT,
    user_id        VARCHAR(20)    NOT NULL,
    password       VARCHAR(100)   NOT NULL,
    name           VARCHAR(10)    NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE maven_files
(
    id             INT            NOT NULL AUTO_INCREMENT,
    file_name      VARCHAR(50)    NOT NULL,
    user_id        INT            NOT NULL,
    project_name   VARCHAR(50)    NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE sonarqube
(
    id             INT            NOT NULL AUTO_INCREMENT,
    sonarqube_name VARCHAR(50)    NOT NULL,
    sonarqube_key  VARCHAR(50)    NOT NULL,
    maven_files_id INT            NOT NULL,
    PRIMARY KEY (id)
);