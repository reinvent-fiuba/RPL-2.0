package com.example.rpl.RPL.model;

import static java.time.ZonedDateTime.now;

import java.time.ZonedDateTime;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @Column(name = "is_io_tested")
    private Boolean isIOTested;

    @Column(name = "points")
    private Long points;

    @Column(name = "compilation_flags")
    private String compilationFlags;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "deleted")
    private Boolean deleted;

    @JoinColumn(name = "starting_files_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RPLFile startingFiles;


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

    public Activity(Course course, ActivityCategory activityCategory, Activity activity) {
        this(
            course,
            activityCategory,
            activity.getName(),
            activity.getDescription(),
            activity.getLanguage(),
            activity.getPoints(),
            new RPLFile(
                String.format("%s_%d_%s.tar.gz", now().toLocalDate().toString(), course.getId(),
                    activity.getName()),
                activity.getStartingFiles()
            ),
            activity.getCompilationFlags(),
            activity.getActive()
        );
        this.isIOTested = activity.getIsIOTested();
    }

    public Activity(Course course, ActivityCategory activityCategory, String name,
        String description, Language language, Long points,
        RPLFile startingFiles, String compilationFlags, Boolean active) {
        this.course = course;
        this.activityCategory = activityCategory;
        this.name = name;
        this.description = description;
        this.language = language;
        this.isIOTested = true;
        this.deleted = false;
        this.startingFiles = startingFiles;
        this.points = points;
        this.dateCreated = now();
        this.lastUpdated = this.dateCreated;
        this.active = active != null ? active : false;
        this.compilationFlags = compilationFlags != null ? compilationFlags : "";
    }

    public void updateActivity(ActivityCategory activityCategory, String name, Boolean active,
        String description, Language language, String compilationFlags, Long score) {
        if (activityCategory != null) {
            this.activityCategory = activityCategory;
        }
        if (name != null) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
        if (language != null) {
            this.language = language;
        }
        if (score != null) {
            this.points = score;
        }
        if (compilationFlags != null) {
            this.compilationFlags = compilationFlags;
        }
        if (active != null) {
            this.active = active;
        }
        this.lastUpdated = now();
    }

    public void setIsIOTested(boolean isIOTested) {
        this.isIOTested = isIOTested;
        this.lastUpdated = now();
    }

    public void setDeleted(boolean isDeleted) {
        this.deleted = isDeleted;
        this.lastUpdated = now();
    }

    public void setActive(Boolean active) {
        this.active = active;
        this.lastUpdated = now();
    }
}
