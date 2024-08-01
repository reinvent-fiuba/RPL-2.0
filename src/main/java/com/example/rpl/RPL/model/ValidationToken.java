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
import lombok.Data;

@Data
@Entity
@Table(name = "validation_token")
public class ValidationToken {

    private static final int EXPIRATION_DAYS = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String token;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(name = "expiry_date")
    private ZonedDateTime expiryDate;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    public ValidationToken() {
    }

    public ValidationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = ZonedDateTime.now().plusDays(EXPIRATION_DAYS);
    }
}
