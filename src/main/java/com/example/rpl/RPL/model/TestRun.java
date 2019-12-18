package com.example.rpl.RPL.model;

import java.time.ZonedDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.hibernate.annotations.Type;

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
    @Column(name = "stderr")
    @Type(type = "clob")
    private String stderr;

    @NonNull
    @Basic(optional = false)
    @Column(name = "stdout")
    @Type(type = "clob")
    private String stdout;

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

}
