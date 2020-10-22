CREATE TABLE activity_submission_comments
(
    id                     BIGINT NOT NULL AUTO_INCREMENT,
    activity_id            BIGINT,
    activity_submission_id BIGINT,
    author_id              BIGINT,
    comment                VARCHAR(10000),
    date_created           DATETIME,
    last_updated           DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (activity_id) REFERENCES activities (id),
    FOREIGN KEY (activity_submission_id) REFERENCES activity_submissions (id),
    FOREIGN KEY (author_id) REFERENCES users (id)
) ENGINE = InnoDB;


ALTER TABLE rpl.activity_submissions
    ADD COLUMN is_shared BOOLEAN DEFAULT false;