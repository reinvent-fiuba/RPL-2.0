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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 *     id           BIGINT NOT NULL AUTO_INCREMENT,
 *     test_id      BIGINT,
 *     success      BOOLEAN,
 *     stderr       VARCHAR(10000),
 *     stdout       VARCHAR(10000),
 *     date_created DATETIME,
 *     last_updated DATETIME,
 *
 *     PRIMARY KEY (id),
 *     FOREIGN KEY (test_id) REFERENCES tests(id)
 */
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "test_results")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;


    @JoinColumn(name = "test_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Test test;

    @Column(name = "success")
    private Boolean success;

    @NonNull
    @Basic(optional = false)
    @Column(name = "stderr")
    private String stderr;

    @NonNull
    @Basic(optional = false)
    @Column(name = "stdout")
    private String stdout;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    private TestResult() {
    }

}
