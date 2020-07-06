package com.example.rpl.RPL.model;

import java.time.ZonedDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "io_tests")
public class IOTest {

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

    @Column(name = "name")
    private String name;

    @Column(name = "test_in")
    private String testIn;

    @Column(name = "test_out")
    private String testOut;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    public IOTest() {
    }

    public IOTest(Long activityId, String name, String testIn, String testOut) {
        this.activityId = activityId;
        this.name = name;
        this.testIn = testIn;
        this.testOut = testOut;
        this.dateCreated = ZonedDateTime.now();
        this.lastUpdated = this.dateCreated;
    }

    public void update(String name, String in, String out) {
        this.name = name;
        this.testIn = in;
        this.testOut = out;
        this.lastUpdated = ZonedDateTime.now();
    }
}
