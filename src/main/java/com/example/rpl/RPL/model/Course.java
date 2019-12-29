package com.example.rpl.RPL.model;

import java.time.ZonedDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Data
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Basic(optional = false)
    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "university_course_id")
    private String universityCourseId;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    public Course() {
    }

    public Course(String name, String universityCourseId) {
        ZonedDateTime now = ZonedDateTime.now();
        this.name = name;
        this.universityCourseId = universityCourseId;
        this.dateCreated = now;
        this.lastUpdated = now;
    }
}
