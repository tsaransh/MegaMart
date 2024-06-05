package com.web.megamart.services;

import com.web.megamart.entity.UserDetail;
import com.web.megamart.payload.AuthenticatedUserDetails;
import com.web.megamart.payload.ChangePassword;
import com.web.megamart.payload.LoginCredentials;
import com.web.megamart.payload.RegisterUser;

import java.util.List;

public interface UserService {

    public boolean registerUser(RegisterUser registerUser);

    public String authenticateUser(LoginCredentials credentials);

    public boolean forgotPassword(String emailOrMobileNumber);

    public void deleteAccount(String emailOrMobileNumber);

    AuthenticatedUserDetails fetchUser(String emailOrMobileNumber);

    UserDetail findByUserUid(String userUid);

    public String generateOTP();

    public boolean verifyAccount(String token);

    public String generateVerificationToken();

    public boolean sendVerificationToken(String token, String email);

    public List<AuthenticatedUserDetails> fetchAllUsers(String emailOrPhone);

    boolean reVerifyAccount(String emailOrMobileNumber);

    boolean changePassword(ChangePassword changePassword);

    String switchProfile(String userUid, String profileType);

    String getUserAccountType(String userUid);

    void save(UserDetail userDetail);
}
