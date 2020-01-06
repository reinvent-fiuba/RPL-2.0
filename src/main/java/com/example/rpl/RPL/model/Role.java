package com.example.rpl.RPL.model;

import static java.time.ZonedDateTime.now;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Data
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "roles")
public class Role {

    private static String PERMISSION_DELIMITER = ",";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "name")
    private String name;

    @Column(name = "permissions")
    private String permissions;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;


    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    public Role() {
    }

    public Role(String name, String permissions) {
        this.name = name;
        this.permissions = permissions;
        this.dateCreated = now();
        this.lastUpdated = this.dateCreated;
    }

    public List<String> getPermissions() {
        return Arrays.asList(this.permissions.split(PERMISSION_DELIMITER));
    }
}
