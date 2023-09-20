package com.example.rpl.RPL.model;

import static java.time.ZonedDateTime.now;

import java.time.ZonedDateTime;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "rpl_files")
public class RPLFile {

    private static String PERMISSION_DELIMITER = ",";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Basic(optional = false)
    @Column(name = "file_name")
    private String fileName;

    @NonNull
    @Basic(optional = false)
    @Column(name = "file_type")
    private String fileType;

    @Lob
    @NonNull
    @Basic(optional = false)
    @Column(name = "data")
    private byte[] data;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;


    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    public RPLFile() {
    }

    public RPLFile(String fileName, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.fileType = contentType;
        this.data = bytes;
        this.dateCreated = now();
        this.lastUpdated = this.dateCreated;
    }

    public RPLFile(String fileName, RPLFile rplFile) {
        this(fileName, rplFile.getFileType(), rplFile.getData());
    }

    public void updateData(byte[] bytes) {
        this.data = bytes;
        this.lastUpdated = now();
    }


}
