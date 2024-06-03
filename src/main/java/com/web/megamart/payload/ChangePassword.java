package com.web.megamart.payload;

import lombok.Data;
import lombok.Getter;

@Data
public class ChangePassword {

    private String emailOrMobileNumber;
    private String otp;
    private String newPassword;

}
