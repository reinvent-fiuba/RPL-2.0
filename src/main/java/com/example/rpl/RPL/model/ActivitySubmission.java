package com.example.rpl.RPL.model;

import static java.time.ZonedDateTime.now;

import java.time.ZonedDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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


@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "activity_submissions")
public class ActivitySubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Activity activity;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "response_files_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RPLFile file;

    @Basic(optional = false)
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    @Basic(optional = false)
    @Column(name = "is_final_solution")
    private Boolean isFinalSolution;

    @Basic(optional = false)
    @Column(name = "is_shared")
    private Boolean isShared;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    public ActivitySubmission() {
    }

    public ActivitySubmission(Activity activity, User user, RPLFile file, SubmissionStatus status) {
        this.activity = activity;
        this.user = user;
        this.file = file;
        this.status = status;
        this.isFinalSolution = false;
        this.isShared = false;
        this.dateCreated = now();
        this.lastUpdated = this.dateCreated;
    }

    public void setEnqueued() {
        this.status = SubmissionStatus.ENQUEUED;
        this.lastUpdated = now();
    }

    public void setProcessedWithError(String stage) {
        this.status = SubmissionStatus.getStatusIfError(stage);
        this.lastUpdated = now();
    }

    public void setProcessedWithTimeOut() {
        this.status = SubmissionStatus.TIME_OUT;
        this.lastUpdated = now();
    }

    public void setProcessedSuccess() {
        this.status = SubmissionStatus.SUCCESS;
        this.lastUpdated = now();
    }

    public void setProcessedFailure() {
        this.status = SubmissionStatus.FAILURE;
        this.lastUpdated = now();
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
        this.lastUpdated = now();
    }

    public void setAsFinalSolution() {
        this.isFinalSolution = true;
        this.lastUpdated = now();
    }

    public void setShared() {
        this.isShared = true;
        this.lastUpdated = now();
    }
}
