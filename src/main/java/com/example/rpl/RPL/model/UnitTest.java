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
@Table(name = "unit_tests")
public class UnitTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

//    @JoinColumn(name = "activity_id", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Activity activity;

    @Column(name = "activity_id")
    private Long activityId;

    @JoinColumn(name = "test_file_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RPLFile testFile;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    public UnitTest() {
    }

    public UnitTest(Long activityId, UnitTest unitTest) {
        this(activityId, new RPLFile("unit_test", unitTest.getTestFile()));
    }

    public UnitTest(Long activityId, RPLFile testFile) {
        this.activityId = activityId;
        this.testFile = testFile;
        this.dateCreated = ZonedDateTime.now();
        this.lastUpdated = this.dateCreated;
    }
}
