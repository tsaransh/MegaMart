package com.web.megamart.payload;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginCredentials {

    private String emailOrMobileNumber;
    private String password;

}
