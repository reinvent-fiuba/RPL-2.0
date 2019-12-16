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

//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user;

    @Basic(optional = false)
    @Column(name = "user_id")
    private Long userId;

    @JoinColumn(name = "response_files_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RPLFile file;

    @Basic(optional = false)
    @Column(name = "status")
    private String status;

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

    public ActivitySubmission(Activity activity, Long userId, RPLFile file, String status) {
        this.activity = activity;
        this.userId = userId;
        this.file = file;
        this.status = status;
        this.dateCreated = now();
        this.lastUpdated = this.dateCreated;
    }
}
