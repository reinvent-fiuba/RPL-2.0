package com.example.rpl.RPL.model;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

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
        ZonedDateTime now = ZonedDateTime.now();
        this.name = name;
        this.permissions = permissions;
        this.dateCreated = now;
        this.lastUpdated = now;
    }

    public List<String> getPermissions() {
        return Arrays.asList(this.permissions.split(PERMISSION_DELIMITER));
    }
}
