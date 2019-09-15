CREATE TABLE roles
(
    id          BIGINT NOT NULL AUTO_INCREMENT,
    name        VARCHAR(50),
    permissions VARCHAR(1000), -- permission1,permission2,permission3,etc
    date_created    DATETIME,
    last_updated    DATETIME,

    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE permissions
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    name         VARCHAR(50),
    date_created DATETIME,

    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE users
(
    id              BIGINT NOT NULL AUTO_INCREMENT,
    name            VARCHAR(255),
    surname         VARCHAR(255),
    student_id      VARCHAR(255),
    email           VARCHAR(255),
    email_validated BOOLEAN,
    degree          VARCHAR(255),
    date_created    DATETIME,
    last_updated    DATETIME,

    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE courses
(
    id                   BIGINT NOT NULL AUTO_INCREMENT,
    name                 VARCHAR(255),
    university_course_id VARCHAR(255),
    date_created         DATETIME,
    last_updated         DATETIME,

    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE courses_semester
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    course_id    bigint,
    description  VARCHAR(255),
    active       BOOLEAN,
    semester     VARCHAR(255),
    date_created DATETIME,
    last_updated DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (course_id) REFERENCES courses (id)
) ENGINE = InnoDB;

CREATE TABLE course_users
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    course_semester_id BIGINT,
    user_id            BIGINT,
    role_id            BIGINT,
    accepted           BOOLEAN,
    date_created       DATETIME,
    last_updated       DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (course_semester_id) REFERENCES courses_semester (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)

) ENGINE = InnoDB;

CREATE TABLE files
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    link_s3      VARCHAR(255),
    date_created DATETIME,
    last_updated DATETIME,

    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE activities
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    course_semester_id BIGINT,
    name               VARCHAR(255),
    description        VARCHAR(255),
    language           VARCHAR(255),
    active             BOOLEAN,
    file_id            BIGINT,
    date_created       DATETIME,
    last_updated       DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (course_semester_id) REFERENCES courses_semester (id),
    FOREIGN KEY (file_id) REFERENCES files (id)
) ENGINE = InnoDB;

CREATE TABLE activity_submissions
(
    id                BIGINT NOT NULL AUTO_INCREMENT,
    activity_id       BIGINT,
    user_id           BIGINT,
    response_files_id BIGINT,
    status            VARCHAR(255),
    date_created      DATETIME,
    last_updated      DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (activity_id) REFERENCES activities (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (response_files_id) REFERENCES files (id)

) ENGINE = InnoDB;

CREATE TABLE tests
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    activity_id  BIGINT,
    date_created DATETIME,
    last_updated DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (activity_id) REFERENCES activities (id)
) ENGINE = InnoDB;

CREATE TABLE results
(
    id                     BIGINT NOT NULL AUTO_INCREMENT,
    activity_submission_id BIGINT,
    test_id                BIGINT,
    score                  varchar(255),
    date_created           DATETIME,
    last_updated           DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (activity_submission_id) REFERENCES activity_submissions (id),
    FOREIGN KEY (test_id) REFERENCES tests (id)
) ENGINE = InnoDB;

CREATE TABLE test_results
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    test_id      BIGINT,
    success      BOOLEAN,
    stderr       TEXT,
    stdout       TEXT,
    date_created DATETIME,
    last_updated DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (test_id) REFERENCES tests (id)
)
    ENGINE = InnoDB;

CREATE TABLE unit_tests
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    test_id      BIGINT,
    test_file_id BIGINT,
    date_created DATETIME,
    last_updated DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (test_id) REFERENCES tests (id),
    FOREIGN KEY (test_file_id) REFERENCES files (id)
) ENGINE = InnoDB;

CREATE TABLE IO_tests
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    test_id      BIGINT,
    test_in      TEXT,
    test_out     TEXT,
    date_created DATETIME,
    last_updated DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (test_id) REFERENCES tests (id)
) ENGINE = InnoDB;