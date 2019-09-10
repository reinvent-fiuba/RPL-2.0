package com.example.rpl.RPL.model;

import java.time.ZonedDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 *     id                 BIGINT NOT NULL AUTO_INCREMENT,
 *     course_semester_id BIGINT,
 *     user_id            BIGINT,
 *     role_id            BIGINT,
 *     accepted           BOOLEAN,
 *     date_created       DATETIME,
 *     last_updated       DATETIME,
 *
 *     PRIMARY KEY (id),
 *     FOREIGN KEY (course_semester_id) REFERENCES courses_semester(id),
 *     FOREIGN KEY (user_id) REFERENCES users(id),
 *     FOREIGN KEY (role_id) REFERENCES roles(id)
 */
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "course_users")
public class CourseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "course_semester_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseSemester courseSemester;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    @Column(name = "accepted")
    private Boolean accepted;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    private CourseUser() {
    }

}
