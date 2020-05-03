package com.example.rpl.RPL.model;

import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "test_run")
public class TestRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;


    @JoinColumn(name = "activity_submission_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private ActivitySubmission activitySubmission;

    @Column(name = "success")
    private Boolean success;

    @NonNull
    @Basic(optional = false)
    @Column(name = "exit_message")
    private String exitMessage;

    @NonNull
    @Basic(optional = false)
    @Column(name = "stderr")
    private String stderr;

    @NonNull
    @Basic(optional = false)
    @Column(name = "stdout")
    private String stdout;

    @OneToMany(mappedBy = "testRun", fetch = FetchType.LAZY)
    private List<IOTestRun> ioTestRunList;

    @OneToMany(mappedBy = "testRun", fetch = FetchType.LAZY)
    private List<UnitTestRun> unitTestRunList;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    public TestRun() {
    }

    public TestRun(ActivitySubmission submission, boolean success, String testRunExitMessage,
        String testRunStderr, String testRunStdout) {
        this.activitySubmission = submission;
        this.success = success;
        this.exitMessage = testRunExitMessage;
        this.stderr = testRunStderr;
        this.stdout = testRunStdout;
        this.dateCreated = ZonedDateTime.now();
        this.lastUpdated = this.dateCreated;
    }
}
