package com.rishab99058.linkedIn.user_service.service.impl;

import com.rishab99058.linkedIn.user_service.dto.*;
import com.rishab99058.linkedIn.user_service.entity.OtpEntity;
import com.rishab99058.linkedIn.user_service.entity.UserEntity;
import com.rishab99058.linkedIn.user_service.exception.AppCommonException;
import com.rishab99058.linkedIn.user_service.repository.OtpEntityRepository;
import com.rishab99058.linkedIn.user_service.repository.UserEntityRepository;
import com.rishab99058.linkedIn.user_service.service.api.AuthService;
import com.rishab99058.linkedIn.user_service.utils.CloudinaryService;
import com.rishab99058.linkedIn.user_service.utils.JwtUtils;
import com.rishab99058.linkedIn.user_service.utils.MailUtil;
import com.rishab99058.linkedIn.user_service.utils.PasswordHandlerUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserEntityRepository userEntityRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final MailUtil mailUtil;
    private final JwtUtils jwtUtils;
    private final OtpEntityRepository otpEntityRepository;

    @Override
    public UserDto createUser(UserDto userDto, MultipartFile profilePictureFile, MultipartFile coverPictureFile) {
        try {
            if(userDto.getPassword() == null || userDto.getPassword().equals("")) {
                throw new AppCommonException("Please `provide the password`");
            }
            if(profilePictureFile!= null) {
                userDto.setProfilePictureUrl(cloudinaryService.uploadFile(profilePictureFile));
            }
            if(coverPictureFile != null) {
                userDto.setCoverPictureUrl(cloudinaryService.uploadFile(coverPictureFile));
            }
            UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
            userEntity.setPassword(PasswordHandlerUtil.hashPassword(userDto.getPassword()));
            userEntity.setCreatedAt(LocalDateTime.now());
            userEntity.setUpdatedAt(LocalDateTime.now());
            userEntity = userEntityRepository.save(userEntity);
            // Send welcome mail
            UserEntity finalUserEntity = userEntity;
            CompletableFuture.runAsync(()-> {
                try {
                    mailUtil.sendWelcomeMail(finalUserEntity.getEmail(), finalUserEntity.getFirstName());
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });
            return modelMapper.map(userEntity, UserDto.class);
        } catch (Exception e) {
            log.error("Error in createUser: {}", e.getMessage(), e);
            throw new AppCommonException("User creation failed");
        }
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        try{
            UserEntity userEntity = userEntityRepository.findByEmailAndDeletedFalse(loginRequestDTO.getEmail());
            if(userEntity == null) {
                throw new AppCommonException("Either user email or password is invalid");
            }

            if(!PasswordHandlerUtil.checkPassword(loginRequestDTO.getPassword(), userEntity.getPassword())) {
                throw new  AppCommonException("Either user email or password is invalid");
            }

            String jwtToken  = jwtUtils.generateJwtToken(userEntity);
            String refreshToken = jwtUtils.generateRefreshToken(userEntity);

            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setRefreshToken(refreshToken);
            loginResponseDTO.setJwtToken(jwtToken);
            loginResponseDTO.setUserDto(modelMapper.map(userEntity, UserDto.class));
            return loginResponseDTO;
        }catch(Exception e)
        {
            log.error("Error in createUser: {}", e.getMessage(), e);
            throw new AppCommonException("User login failed");
        }
    }

    @Override
    public BaseDTO forgotPassword(ForgotPasswordDTO loginRequestDTO) {
        try{
            BaseDTO response  = new BaseDTO();
            UserEntity user  = userEntityRepository.findByEmailAndDeletedFalse(loginRequestDTO.getEmail());
            if(user == null)
            {
                throw new Exception("User doesn't exist with the given email id");
            }
            OtpEntity otpEntity = otpEntityRepository.findByEmailAndDeletedAtNullAndValidTrue(loginRequestDTO.getEmail());
            if(otpEntity != null)
            {

                otpEntity.setDeletedAt(new  Date());
                otpEntity.setValid(false);
                otpEntityRepository.save(otpEntity);
            }
            OtpEntity otp  = prepareOtp(loginRequestDTO.getEmail());
            otp = otpEntityRepository.save(otp);
            mailUtil.sendForgotPasswordOtpMail(loginRequestDTO.getEmail(),user.getFirstName(),otp.getOtp());
            response.setMessage("Rest OTP has been sent to the user");
            return response;
        }catch(Exception e)
        {
            log.error("Error in forgotPassword: {}", e.getMessage(), e);
            throw new AppCommonException("Exception in sending reset password mail");
        }
    }

    @Override
    public BaseDTO resetPassword(ResetPasswordDTO resetPasswordDTO) {
        try{
            BaseDTO response  = new BaseDTO();
            UserEntity user = userEntityRepository.findByEmailAndDeletedFalse(resetPasswordDTO.getEmail());
            if (user == null) {
                throw new Exception("User doesn't exist with the given email id");
            }

            OtpEntity otpEntity = otpEntityRepository.findByEmailAndDeletedAtNullAndValidTrue(resetPasswordDTO.getEmail());
            if (otpEntity == null) {
                throw new Exception("Something went wrong, please request a new OTP");
            }

            LocalDateTime otpCreatedAt = otpEntity.getCreatedAt();
            if (otpCreatedAt.plusMinutes(15).isBefore(LocalDateTime.now()) || !otpEntity.isValid()) {
                otpEntity.setDeletedAt(new Date());
                otpEntity.setValid(false);
                otpEntityRepository.save(otpEntity);
                OtpEntity newOtp = prepareOtp(resetPasswordDTO.getEmail());
                otpEntityRepository.save(newOtp);
                mailUtil.sendForgotPasswordOtpMail(resetPasswordDTO.getEmail(),user.getFirstName(),newOtp.getOtp());
                response.setMessage("OTP has been expired, New OTP has been sent on the mail");
                return response;
            }

            if (!otpEntity.getOtp().equals(resetPasswordDTO.getOtp())) {
                throw new Exception("OTP is incorrect. Please try again.");
            }
            user.setPassword(PasswordHandlerUtil.hashPassword(resetPasswordDTO.getPassword()));
            user.setUpdatedAt(LocalDateTime.now());
            userEntityRepository.save(user);
            otpEntity.setValid(false);
            otpEntity.setDeletedAt(new Date());
            otpEntityRepository.save(otpEntity);
            response.setMessage("Password has been reset successfully");
            return response;
        }catch(Exception e)
        {
            log.error("Error in Reset Password: {}", e.getMessage(), e);
            throw new AppCommonException("Exception in  reset password");
        }
    }

    private OtpEntity prepareOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setValid(true);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setUpdatedAt(new Date());
        otpEntity.setDeletedAt(null);

        return otpEntity;
    }
}
