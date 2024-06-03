package com.web.megamart.payload;

import lombok.Data;
import lombok.Getter;

@Data
public class RegisterUser {

    private String fullName;
    private String email;
    private String password;
    private String mobileNumber;

}
