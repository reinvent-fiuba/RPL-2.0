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
import lombok.NonNull;
import lombok.ToString;

/**
 *     id                 BIGINT NOT NULL AUTO_INCREMENT,
 *     course_semester_id BIGINT,
 *     name               VARCHAR(255),
 *     description        VARCHAR(255),
 *     language           VARCHAR(255),
 *     active             BOOLEAN,
 *     file_id            BIGINT,
 *     date_created       DATETIME,
 *     last_updated       DATETIME,
 *
 *     PRIMARY KEY (id),
 *     FOREIGN KEY (course_semester_id) REFERENCES courses_semester(id),
 *     FOREIGN KEY (file_id) REFERENCES files(id)
 */
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "course_semester_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseSemester courseSemester;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @NonNull
    @Basic(optional = false)
    @Column(name = "description")
    private String description;

    @NonNull
    @Basic(optional = false)
    @Column(name = "language")
    private Language language;

    @Column(name = "active")
    private Boolean active;

    @JoinColumn(name = "file_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RPLFile file;


    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    private Activity() {
    }

}
