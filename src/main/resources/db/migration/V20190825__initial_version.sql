CREATE TABLE roles
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    name         VARCHAR(50),
    permissions  VARCHAR(1000), -- permissionName1,permissionName2,permissionName3,etc
    date_created DATETIME,
    last_updated DATETIME,

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
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    name            VARCHAR(255),
    surname         VARCHAR(255),
    student_id      VARCHAR(255),
    username        VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    email_validated BOOLEAN      NOT NULL,
    is_admin        BOOLEAN DEFAULT FALSE,
    degree          VARCHAR(255),
    university      VARCHAR(255),
    date_created    DATETIME,
    last_updated    DATETIME,

    PRIMARY KEY (id),
    UNIQUE (email),
    UNIQUE (username)
) ENGINE = InnoDB;

CREATE TABLE courses
(
    id                   BIGINT NOT NULL AUTO_INCREMENT,
    name                 VARCHAR(255),
    university_course_id VARCHAR(255),
    description          VARCHAR(255),
    active               BOOLEAN,
    deleted              BOOLEAN,
    semester             VARCHAR(255),
    img_uri              VARCHAR(255),
    date_created         DATETIME,
    last_updated         DATETIME,

    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE course_users
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    course_id    BIGINT,
    user_id      BIGINT,
    role_id      BIGINT,
    accepted     BOOLEAN,
    date_created DATETIME,
    last_updated DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (course_id) REFERENCES courses (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)

) ENGINE = InnoDB;

CREATE TABLE rpl_files
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    file_name    VARCHAR(255),
    file_type    VARCHAR(255),
    data         BLOB,
    date_created DATETIME,
    last_updated DATETIME,

    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE activity_categories
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    course_id    BIGINT,
    name         VARCHAR(255),
    description  VARCHAR(255),
    active       BOOLEAN,
    date_created DATETIME,
    last_updated DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (course_id) REFERENCES courses (id)
) ENGINE = InnoDB;

CREATE TABLE activities
(
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    course_id            BIGINT,
    activity_category_id BIGINT,
    name                 VARCHAR(500),
    description          VARCHAR(20000),
    language             VARCHAR(255),
    is_io_tested         BOOLEAN,
    active               BOOLEAN,
    deleted              BOOLEAN,
    starting_files_id    BIGINT,
    points               BIGINT,
    compilation_flags    VARCHAR(500) NOT NULL DEFAULT '',
    date_created         DATETIME,
    last_updated         DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (course_id) REFERENCES courses (id),
    FOREIGN KEY (activity_category_id) REFERENCES activity_categories (id),
    FOREIGN KEY (starting_files_id) REFERENCES rpl_files (id)
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
    FOREIGN KEY (response_files_id) REFERENCES rpl_files (id)

) ENGINE = InnoDB;

CREATE TABLE results
(
    id                     BIGINT NOT NULL AUTO_INCREMENT,
    activity_submission_id BIGINT,
    score                  varchar(255),
    date_created           DATETIME,
    last_updated           DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (activity_submission_id) REFERENCES activity_submissions (id)
) ENGINE = InnoDB;

CREATE TABLE test_run
(
    id                     BIGINT NOT NULL AUTO_INCREMENT,
    activity_submission_id BIGINT,
    success                BOOLEAN,
    exit_message           varchar(255),
    stderr                 varchar(20000),
    stdout                 varchar(20000),
    date_created           DATETIME,
    last_updated           DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (activity_submission_id) REFERENCES activity_submissions (id)
)
    ENGINE = InnoDB;

CREATE TABLE unit_tests
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    activity_id  BIGINT,
    test_file_id BIGINT,
    date_created DATETIME,
    last_updated DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (activity_id) REFERENCES activities (id),
    FOREIGN KEY (test_file_id) REFERENCES rpl_files (id)
) ENGINE = InnoDB;

CREATE TABLE IO_tests
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    activity_id  BIGINT,
    name         VARCHAR(500),
    test_in      VARCHAR(5000),
    test_out     VARCHAR(5000),
    date_created DATETIME,
    last_updated DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (activity_id) REFERENCES activities (id)
) ENGINE = InnoDB;

CREATE TABLE io_test_run
(
    id              BIGINT NOT NULL AUTO_INCREMENT,
    test_run_id     BIGINT,
    test_name       VARCHAR(500),
    test_in         VARCHAR(5000),
    expected_output VARCHAR(5000),
    run_output      VARCHAR(5000),
    date_created    DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (test_run_id) REFERENCES test_run (id)
) ENGINE = InnoDB;

CREATE TABLE unit_test_run
(
    id             BIGINT NOT NULL AUTO_INCREMENT,
    test_run_id    BIGINT,
    name           VARCHAR(255),
    passed         boolean,
    error_messages VARCHAR(5000),
    date_created   DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (test_run_id) REFERENCES test_run (id)
) ENGINE = InnoDB;


CREATE TABLE validation_token
(
    id          BIGINT NOT NULL AUTO_INCREMENT,
    user_id     BIGINT,
    token       VARCHAR(255),
    expiry_date DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE = InnoDB;

