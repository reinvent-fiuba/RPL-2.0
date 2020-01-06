package com.example.rpl.RPL.model;

import java.time.ZonedDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @Column(name = "name")
    private String name;

    @Column(name = "university_course_id")
    private String universityCourseId;

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
    public Course() {
    }

    public Course(String name, String universityCourseId, String description, Boolean active,
        String semester, String imgUri) {
        ZonedDateTime now = ZonedDateTime.now();
        this.name = name;
        this.universityCourseId = universityCourseId;
        this.description = description;
        this.active = active;
        this.semester = semester;
        this.imgUri = imgUri;
        this.dateCreated = now;
        this.lastUpdated = now;
    }
}
