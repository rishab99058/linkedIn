package com.rishab99058.linkedIn.user_service.service.api;

import com.rishab99058.linkedIn.user_service.dto.*;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {

    UserDto createUser(UserDto userDto, MultipartFile profilePictureFile, MultipartFile coverPictureFile);
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    BaseDTO forgotPassword(ForgotPasswordDTO loginRequestDTO);
    BaseDTO resetPassword(ResetPasswordDTO resetPasswordDTO);

}
