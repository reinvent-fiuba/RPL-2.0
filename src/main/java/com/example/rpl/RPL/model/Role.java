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
 id   BIGINT NOT NULL AUTO_INCREMENT,
 name VARCHAR(50),
 permissions VARCHAR(1000), # permission1,permission2,permission3,etc
 */
@Getter
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

    public String[] getPermissions() {
        return this.permissions.split(PERMISSION_DELIMITER);
    }
}
