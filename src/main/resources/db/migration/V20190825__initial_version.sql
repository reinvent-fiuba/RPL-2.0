CREATE TABLE task
(
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    course_id      VARCHAR(45)  NOT NULL,
    user_id        BIGINT       NOT NULL,
    solver_user_id BIGINT       NULL,
    status         VARCHAR(255) NOT NULL,
    date_created   DATETIME,
    last_updated   DATETIME,

    PRIMARY KEY (id)
) ENGINE = InnoDB;
