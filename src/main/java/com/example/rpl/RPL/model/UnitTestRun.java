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
@Table(name = "unit_test_run")
public class UnitTestRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "test_run_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TestRun testRun;

    @Column(name = "name")
    String name;

    @Column(name = "passed")
    Boolean passed;

    @Column(name = "error_messages")
    String errorMessages;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    public UnitTestRun() {
    }

    public UnitTestRun(TestRun testRun, String name, Boolean passed, String errorMessages) {
        this.testRun = testRun;
        this.name = name;
        this.passed = passed;
        this.errorMessages = errorMessages;
        this.dateCreated = ZonedDateTime.now();
    }
}
