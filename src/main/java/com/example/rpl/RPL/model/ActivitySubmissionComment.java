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
@Table(name = "activity_submission_comments")
public class ActivitySubmissionComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Activity activity;

    @JoinColumn(name = "activity_submission_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ActivitySubmission activitySubmission;

    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @NonNull
    @Basic(optional = false)
    @Column(name = "comment")
    private String comment;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    public ActivitySubmissionComment() {
    }

    public ActivitySubmissionComment(Activity activity, ActivitySubmission activitySubmission,
        User author, String comment) {
        this.activity = activity;
        this.author = author;
        this.activitySubmission = activitySubmission;
        this.comment = comment;
        this.dateCreated = now();
        this.lastUpdated = this.dateCreated;
    }
}
