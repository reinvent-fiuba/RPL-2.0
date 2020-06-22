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
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "io_test_run")
public class IOTestRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "test_run_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TestRun testRun;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "test_in")
    private String testIn;

    @Column(name = "expected_output")
    private String expectedOutput;

    @Column(name = "run_output")
    private String runOutput;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    public IOTestRun() {
    }

    public IOTestRun(TestRun testRun, String testName, String testIn, String expectedOutput,
        String runOutput) {
        this.testRun = testRun;
        this.testName = testName;
        this.testIn = testIn;
        this.expectedOutput = expectedOutput;
        this.runOutput = runOutput;
        this.dateCreated = ZonedDateTime.now();
    }
}
