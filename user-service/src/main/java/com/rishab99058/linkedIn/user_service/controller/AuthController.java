package com.rishab99058.linkedIn.user_service.controller;

import com.rishab99058.linkedIn.user_service.dto.*;
import com.rishab99058.linkedIn.user_service.service.api.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign_up")
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestPart(value = "user", required = true) UserDto userDto,
            @RequestPart(value = "profile_picture", required = false) MultipartFile profilePictureFile,
            @RequestPart(value = "cover_picture", required = false) MultipartFile coverPictureFile
    ) {
        UserDto createdUser = authService.createUser(userDto, profilePictureFile, coverPictureFile);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/log_in")
    public ResponseEntity<LoginResponseDTO>  login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<BaseDTO> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        BaseDTO responseDTO = authService.forgotPassword(forgotPasswordDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/reset_password")
    public ResponseEntity<BaseDTO> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        BaseDTO responseDTO = authService.resetPassword(resetPasswordDTO);
        return ResponseEntity.ok(responseDTO);
    }

}