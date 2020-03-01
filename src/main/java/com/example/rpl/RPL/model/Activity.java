package com.example.rpl.RPL.model;

import static java.time.ZonedDateTime.now;

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

    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @JoinColumn(name = "activity_category_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ActivityCategory activityCategory;

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

    @NonNull
    @Basic(optional = true)
    @Column(name = "initial_code")
    private String initialCode;

    @JoinColumn(name = "supporting_file_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RPLFile supportingFile;


    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    public Activity() {
    }

    public Activity(Course course, ActivityCategory activityCategory, String name,
        String description, Language language, String initialCode,
        RPLFile supportingFile) {
        this.course = course;
        this.activityCategory = activityCategory;
        this.name = name;
        this.description = description;
        this.language = language;
        this.active = true;
        this.supportingFile = supportingFile;
        this.initialCode = initialCode;
        this.dateCreated = now();
        this.lastUpdated = this.dateCreated;
    }

    public void updateActivity(ActivityCategory activityCategory, String name,
        String description, Language language, String initialCode) {
        this.activityCategory = activityCategory;
        this.name = name;
        this.description = description;
        this.language = language;
        this.initialCode = initialCode;
        this.lastUpdated = now();
    }
}
