package com.web.megamart.controller;

import com.web.megamart.exception.UserException;
import com.web.megamart.payload.AuthenticatedUserDetails;
import com.web.megamart.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<AuthenticatedUserDetails> getUserDetail(@RequestParam String emailOrMobileNumber) {
        return ResponseEntity.ok(userService.fetchUser(emailOrMobileNumber));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam String emailOrMobileNumber) {
        userService.deleteAccount(emailOrMobileNumber);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/fetch/all")
    public ResponseEntity<List<AuthenticatedUserDetails>> fetchAllUsers(@RequestParam String emailOrMobileNumber) {
       return ResponseEntity.ok(userService.fetchAllUsers(emailOrMobileNumber));
    }

    @GetMapping("/switch-profile")
    public ResponseEntity<String> switchProfile(@RequestParam String userUid, String profileType) {
        return ResponseEntity.ok(userService.switchProfile(userUid, profileType));
    }

    @GetMapping("/get-profile")
    public ResponseEntity<String> getUserAccountType(@RequestParam String userUid) {
        return ResponseEntity.ok(userService.getUserAccountType(userUid));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
    }

}
