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
import lombok.NonNull;
import lombok.ToString;

/**
 *     id              BIGINT NOT NULL AUTO_INCREMENT,
 *     name            VARCHAR(255),
 *     surname         VARCHAR(255),
 *     student_id      VARCHAR(255),
 *     email           VARCHAR(255),
 *     email_validated BOOLEAN,
 *     degree          VARCHAR(255),
 *     date_created    DATETIME,
 *     last_updated    DATETIME,
 */
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Basic(optional = false)
    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "student_id")
    private String student_id;

    @Basic(optional = false)
    @Column(name = "email")
    private String email;

    @Basic(optional = false)
    @Column(name = "email_validated")
    private Boolean email_validated;

    @Column(name = "degree")
    private String degree;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    private User() {
    }

    public User(String name, String email) {
        this. name = name;
        this.email = email;
        this.email_validated = false;
        this.dateCreated = ZonedDateTime.now();
        this.lastUpdated = ZonedDateTime.now();
    }

}
