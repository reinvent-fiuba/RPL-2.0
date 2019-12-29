package com.example.rpl.RPL.model;

import java.net.URI;
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

import lombok.*;

@Data
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "courses_semester")
public class CourseSemester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "semester")
    private String semester;

    @Column(name = "img_uri")
    private String imgUri;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    public CourseSemester() {
    }

    public CourseSemester(Course course, String description, Boolean active, String semester, String imgUri) {
        ZonedDateTime now = ZonedDateTime.now();
        this.course = course;
        this.description = description;
        this.active = active;
        this.semester = semester;
        this.imgUri = imgUri;
        this.dateCreated = now;
        this.lastUpdated = now;
    }
}
