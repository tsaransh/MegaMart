package com.web.megamart.payload;

import lombok.Data;
import lombok.Getter;

@Data
public class LoginCredentials {

    private String emailOrMobileNumber;
    private String password;

}
