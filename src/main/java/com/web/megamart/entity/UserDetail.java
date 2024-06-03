package com.web.megamart.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="user_details")
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString
public class UserDetail {

    @Id
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @NotNull
    private String fullName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String mobileNumber;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @NotNull
    private String accountType;

    @NotNull
    private boolean isAccountVerified = false;

    private String otp;

    @NotNull
    private String role = "USER";

    private String verificationToken;

    private String userAddress;

    public boolean getAccountVerified() {
        return isAccountVerified;
    }
}
