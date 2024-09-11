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

    @Column(name = "university")
    private String university;

    @Column(name = "university_course_id")
    private String universityCourseId;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "semester")
    private String semester;

    @Column(name = "semester_start_date")
    private ZonedDateTime semesterStartDate;

    @Column(name = "semester_end_date")
    private ZonedDateTime semesterEndDate;

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

    public Course(String name, String university, String universityCourseId, String description, Boolean active,
        String semester, ZonedDateTime semesterStartDate, ZonedDateTime semesterEndDate, String imgUri) {
        ZonedDateTime now = ZonedDateTime.now();
        this.name = name;
        this.university = university;
        this.universityCourseId = universityCourseId;
        this.description = description;
        this.active = active;
        this.deleted = false;
        this.semester = semester;
        this.semesterEndDate = semesterEndDate;
        this.semesterStartDate = semesterStartDate;
        this.imgUri = imgUri;
        this.dateCreated = now;
        this.lastUpdated = now;
    }
}
