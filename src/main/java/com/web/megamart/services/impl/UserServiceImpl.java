package com.web.megamart.services.impl;

import com.web.megamart.doa.UserRepository;
import com.web.megamart.entity.helper.AccountType;
import com.web.megamart.entity.UserDetail;
import com.web.megamart.exception.UserException;
import com.web.megamart.payload.*;
import com.web.megamart.security.JwtHelper;
import com.web.megamart.services.MailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements com.web.megamart.services.UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final MailService mailService;



    private Random random = new Random();

    @Value("${app.host.url}")
    private String appHostUrl;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, AuthenticationManager authenticationManager, JwtHelper jwtHelper, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
        this.mailService = mailService;
    }

    @Override
    public boolean registerUser(RegisterUser registerUser) {
        if (userRepository.existsByEmail(registerUser.getEmail())) {
            throw new UserException(registerUser.getEmail() + " is already registered with another account.", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByMobileNumber(registerUser.getMobileNumber())) {
            throw new UserException(registerUser.getMobileNumber() + " is already registered with another account.", HttpStatus.BAD_REQUEST);
        }

        UserDetail userDetails = new UserDetail();
        userDetails.setFullName(registerUser.getFullName());
        userDetails.setEmail(registerUser.getEmail());
        userDetails.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        userDetails.setMobileNumber(registerUser.getMobileNumber());
        userDetails.setAccountType(AccountType.BUYER.toString());

        String verificationToken = generateVerificationToken();
        userDetails.setVerificationToken(verificationToken);

        UserDetail savedUser = userRepository.save(userDetails);
        if(savedUser == null) {
            throw new UserException("Something went wrong, please try again later.", HttpStatus.BAD_REQUEST);
        }

        sendVerificationToken(verificationToken, savedUser.getEmail());

        return savedUser.getId() != null;
    }

    @Override
    public String authenticateUser(LoginCredentials credentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getEmailOrMobileNumber(), credentials.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtHelper.generateToken(authentication);
    }

    @Override
    public boolean forgotPassword(String emailOrMobileNumber) {
        UserDetail userDetail = userRepository.findByEmailOrMobileNumber(emailOrMobileNumber, emailOrMobileNumber)
                .orElseThrow(() -> new UserException("User Not found with: " + emailOrMobileNumber, HttpStatus.NOT_FOUND));

        if(!isAccountVerified(userDetail)) {
            reVerifyAccount(emailOrMobileNumber);
            throw new UserException("Your account is not verified, Please check your email", HttpStatus.BAD_REQUEST);
        }

        String otp = generateOTP();
        userDetail.setOtp(otp);

        userRepository.save(userDetail);

        MailData mailData = new MailData();

        mailData.setSendTo(userDetail.getEmail());
        mailData.setSubject("forgot password");
        mailData.setMessage("Your One-Time-Password (OTP) to change MegaMart password is "
                +otp+
                " The OTP only valid for 10min. Please don't share OTP with any one");

        return sendEmail(mailData);
    }

    private boolean isAccountVerified(UserDetail userDetail) {
        return userDetail.getAccountVerified();
    }

    @Override
    public void deleteAccount(String emailOrMobileNumber) {

        UserDetail userDetail = userRepository.findByEmailOrMobileNumber(emailOrMobileNumber, emailOrMobileNumber)
                .orElseThrow(() -> new UserException("User Not found with: " + emailOrMobileNumber, HttpStatus.NOT_FOUND));

        if(!isAccountVerified(userDetail)) {
            reVerifyAccount(emailOrMobileNumber);
            throw new UserException("Your account is not verified", HttpStatus.BAD_REQUEST);
        }

        userRepository.delete(userDetail);
    }

    @Override
    public AuthenticatedUserDetails fetchUser(String emailOrMobileNumber) {
        UserDetail userDetail = userRepository.findByEmailOrMobileNumber(emailOrMobileNumber, emailOrMobileNumber)
                .orElseThrow(() -> new UserException("User not found with: " + emailOrMobileNumber, HttpStatus.NOT_FOUND));

        if(!isAccountVerified(userDetail)) {
            reVerifyAccount(emailOrMobileNumber);
            throw new UserException("Your account is not verified, Please check your email", HttpStatus.BAD_REQUEST);
        }

        return modelMapper.map(userDetail, AuthenticatedUserDetails.class);
    }

    @Override
    public UserDetail findByUserUid(String userUid) {
        UserDetail userDetail = userRepository.findById(userUid)
                .orElseThrow(() -> new UserException("User not found with id: "+userUid, HttpStatus.NOT_FOUND));
        return userDetail;
    }

    @Override
    public String generateOTP() {
        int otpLength = 6;
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<otpLength;i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }


    @Override
    public boolean verifyAccount(String token) {
        UserDetail userDetail = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new UserException("Invalid Token! Please re-verify your account", HttpStatus.BAD_REQUEST));

        userDetail.setAccountVerified(true);
        userDetail.setVerificationToken(null);

        return userRepository.save(userDetail).getAccountVerified()? true : false;
    }

    @Override
    public String generateVerificationToken() {
        String verificationToken = UUID.randomUUID().toString();
        return verificationToken;
    }

    @Override
    public boolean sendVerificationToken(String token, String email) {
        String verificationUrl = appHostUrl+"/api/v1/user/auth/verify?token="+token;
        MailData mailData = new MailData();
        mailData.setSendTo(email);
        mailData.setSubject("no-reply");
        mailData.setMessage("Verify your email id with MegaMart\n\n"+verificationUrl);
        return sendEmail(mailData);
    }

    private boolean sendEmail(MailData mailData) {
        try {
            return mailService.sendMail(mailData);
        } catch (Exception e) {
            throw new UserException("Failed to send email", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<AuthenticatedUserDetails> fetchAllUsers(String emailOrMobileNumber) {

        AuthenticatedUserDetails authenticatedUserDetails = fetchUser(emailOrMobileNumber);

        if ("ADMIN".equals(authenticatedUserDetails.getRole())) {
            return userRepository.findAll().stream()
                    .map(user -> modelMapper.map(user, AuthenticatedUserDetails.class))
                    .collect(Collectors.toList());
        } else {
            throw new UserException("Oops something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public boolean reVerifyAccount(String emailOrMobileNumber) {

        UserDetail userDetail = userRepository.findByEmailOrMobileNumber(emailOrMobileNumber, emailOrMobileNumber)
                .orElseThrow(() -> new UserException("User not found with: "+emailOrMobileNumber, HttpStatus.BAD_REQUEST));
        if(userDetail.getAccountVerified()) {
            throw new UserException("Account is already verify", HttpStatus.BAD_REQUEST);
        }
            String token = generateVerificationToken();
            userDetail.setVerificationToken(token);
            userDetail.setAccountVerified(false);

            UserDetail savedUser = userRepository.save(userDetail);

            return sendVerificationToken(token, savedUser.getEmail());

    }

    @Override
    public boolean changePassword(ChangePassword changePassword) {

        if(changePassword.getOtp().isEmpty()) {
            throw new UserException("Please enter 6 digit OTP send to your registered email id", HttpStatus.BAD_REQUEST);
        }

        String findId = changePassword.getEmailOrMobileNumber();
        UserDetail userDetail = userRepository.findByEmailOrMobileNumber(findId, findId)
                .orElseThrow(() -> new UserException("User not found with "+findId, HttpStatus.BAD_REQUEST));

        if(!isAccountVerified(userDetail)) {
            reVerifyAccount(findId);
            throw new UserException("Your account is not verified, Please check your email", HttpStatus.BAD_REQUEST);
        }

        if(userDetail.getOtp().compareTo(changePassword.getOtp()) == 0) {
            userDetail.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
            userDetail.setOtp(null);
            userRepository.save(userDetail);
            return true;
        }
        else {
            throw new UserException("Invalid Otp", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String switchProfile(String userUid, String profileType) {
        UserDetail userDetail = findByUserUid(userUid);
        if(userDetail.getAccountType().compareTo(profileType.toUpperCase()) == 0) {
            throw  new UserException("Your profile is already set as "+profileType, HttpStatus.BAD_REQUEST);
        }
        userDetail.setAccountType(profileType.toUpperCase());
        UserDetail savedUser = userRepository.save(userDetail);
        return "Profile change to "+profileType.toUpperCase();
    }

    @Override
    public String getUserAccountType(String userUid) {
        UserDetail userDetail = findByUserUid(userUid);
        return userDetail.getAccountType();
    }

    @Override
    public void save(UserDetail userDetail) {
        userRepository.save(userDetail);
    }

}
