package com.web.megamart.payload;

import lombok.Data;

import java.util.Date;

@Data
public class AuthenticatedUserDetails {

    private String userId;
    private String fullName;
    private String email;
    private String mobileNumber;
    private Date createdDate;
    private String accountType;
    private boolean isAccountVerified;
    private String role;

}
