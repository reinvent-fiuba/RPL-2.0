package com.example.rpl.RPL.model;

import java.time.ZonedDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.NonNull;

@Data
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
    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "student_id")
    private String studentId;

    @NonNull
    @Pattern(regexp = "^[a-zA-Z0-9_-]{6,12}$")
    @Basic(optional = false)
    @Column(name = "username", unique = true)
    private String username;

    @NonNull
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Basic(optional = false)
    @Column(name = "email", unique = true)
    private String email;

    @NonNull
    @Basic(optional = false)
    @Column(name = "password")
    private String password;

    @NonNull
    @Basic(optional = false)
    @Column(name = "email_validated")
    private Boolean emailValidated;

    @Column(name = "university")
    private String university;

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
    public User() {
    }

    public User(String name, String surname, String studentId, String username, String email,
        String encodedPassword, String university, String degree) {
        ZonedDateTime now = ZonedDateTime.now();
        this.name = name;
        this.surname = surname;
        this.studentId = studentId;
        this.username = username;
        this.email = email;
        this.password = encodedPassword;
        this.university = university;
        this.degree = degree;
        this.emailValidated = false;
        this.dateCreated = now;
        this.lastUpdated = now;
    }
}
