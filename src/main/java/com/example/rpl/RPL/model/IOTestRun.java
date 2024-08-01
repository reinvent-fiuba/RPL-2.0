package com.example.rpl.RPL.model;

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
