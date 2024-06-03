package com.web.megamart.controller;

import com.web.megamart.exception.UserException;
import com.web.megamart.payload.AuthenticatedUserDetails;
import com.web.megamart.payload.ChangePassword;
import com.web.megamart.payload.LoginCredentials;
import com.web.megamart.payload.RegisterUser;
import com.web.megamart.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authUser(@RequestBody LoginCredentials credentials) {
        String token = userService.authenticateUser(credentials);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUser registerUser) {
        userService.registerUser(registerUser);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String emailOrMobileNumber) {
        boolean otpSend = userService.forgotPassword(emailOrMobileNumber);
        return otpSend
                ? ResponseEntity.ok("OTP send")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Opt not send");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        boolean verifyComplete = userService.verifyAccount(token);
        return verifyComplete ? ResponseEntity.ok("Your account has been verified")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to verify your account!");
    }

    @GetMapping("/re-verify")
    public ResponseEntity<String> reVerifyAccount(@RequestParam String emailOrMobileNumber) {
        boolean verifyComplete = userService.reVerifyAccount(emailOrMobileNumber);
        return verifyComplete ? ResponseEntity.ok("Your account has been verified")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to verify your account!");

    }

    @PostMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangePassword changePassword) {

        boolean passwordChanged = userService.changePassword(changePassword);

        return ResponseEntity.ok(true);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
    }
}
