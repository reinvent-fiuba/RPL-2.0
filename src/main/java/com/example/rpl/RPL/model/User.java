package com.example.rpl.RPL.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NonNull;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 2405172041950251807L;

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
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,}$")
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

    @Basic(optional = false)
    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "university")
    private String university;

    @Column(name = "degree")
    private String degree;

    @Column(name = "img_uri")
    private String imgUri;

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
        this.isAdmin = false;
        this.dateCreated = now;
        this.lastUpdated = now;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.lastUpdated = ZonedDateTime.now();
    }

    public void markAsValidated() {
        this.emailValidated = true;
        this.lastUpdated = ZonedDateTime.now();
    }
}
