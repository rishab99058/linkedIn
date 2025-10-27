package com.rishab99058.linkedIn.user_service.controller;

import com.rishab99058.linkedIn.user_service.dto.ExperienceDto;
import com.rishab99058.linkedIn.user_service.dto.UserDto;
import com.rishab99058.linkedIn.user_service.service.api.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get_user")
    public ResponseEntity<UserDto> getUser(@RequestParam(name = "id", required = true) String id) {

            UserDto userDto = userService.getUserById(id);
            return ResponseEntity.ok(userDto);

    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filter
    ) {
        List<UserDto> users = userService.getAllUsers(page, size, filter);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/update")
    public ResponseEntity<UserDto> updateUser(
            @RequestParam("id") String id,
            @RequestPart("user") UserDto userDto,
            @RequestParam(value = "profile_picture", required = false) MultipartFile profilePictureFile,
            @RequestParam(value = "cover_picture", required = false) MultipartFile coverPictureFile
    ) {
        UserDto updatedUser = userService.updateUser(id, userDto, profilePictureFile, coverPictureFile);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> softDeleteUser(@RequestParam("id") String id) {
        try {
            userService.softDeleteUser(id);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User soft-delete failed: " + e.getMessage());
        }
    }

    @PostMapping("/deactivate")
    public ResponseEntity<String> deactivateUser(@RequestParam("id") String id) {
        try {
            userService.deactivateUser(id);
            return ResponseEntity.ok("User deactivated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User deactivation failed: " + e.getMessage());
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activateUser( HttpServletRequest httpServletRequest) {
        try {
            String id = httpServletRequest.getHeader("X-User-ID");
            userService.activateUser(id);
            return ResponseEntity.ok("User activated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User activation failed: " + e.getMessage());
        }
    }

    @PostMapping("/update_profile_picture")
    public ResponseEntity<String> updateProfilePicture(
            @RequestParam("id") String id,
            @RequestParam("profile_picture") MultipartFile profilePictureFile) {
        try {
            userService.updateProfilePicture(id, profilePictureFile);
            return ResponseEntity.ok("Profile picture updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Profile picture update failed: " + e.getMessage());
        }
    }

    @PostMapping("/update_cover_picture")
    public ResponseEntity<String> updateCoverPicture(
            HttpServletRequest httpServletRequest,
            @RequestParam("cover_picture") MultipartFile coverPictureFile) {
        try {
            String userId = httpServletRequest.getHeader("X-User-Id");
            userService.updateCoverPicture(userId, coverPictureFile);
            return ResponseEntity.ok("Cover picture updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cover picture update failed: " + e.getMessage());
        }
    }

    @GetMapping("/experiences")
    public ResponseEntity<List<ExperienceDto>> getExperiences(@RequestParam("user_id") String userId) {
        List<ExperienceDto> experiences = userService.getExperiences(userId);
        return ResponseEntity.ok(experiences);
    }

    @PostMapping("/add_experience")
    public ResponseEntity<ExperienceDto> addExperience(
            @RequestParam("user_id") String userId,
            @Valid @RequestBody ExperienceDto experienceDto) {
        ExperienceDto addedExperience = userService.addExperience(userId, experienceDto);
        return ResponseEntity.ok(addedExperience);
    }

    @DeleteMapping("/remove_experience")
   public ResponseEntity<String> removeExperience(@RequestParam("user_id") String userId, @RequestParam("id") String id)
    {
        userService.deleteExperience(userId, id);
        return ResponseEntity.ok("Experience removed successfully");
    }

    @PutMapping("/update_experience")
    public ResponseEntity<ExperienceDto> updateExperience(
            @RequestParam("user_id") String userId,
            @RequestParam("exp_id") String expId,
            @Valid @RequestBody ExperienceDto experienceDto) {
        ExperienceDto updatedExperience = userService.updateExperience(userId, expId, experienceDto);
        return ResponseEntity.ok(updatedExperience);
    }

    @GetMapping("/experience")
    public ResponseEntity<ExperienceDto> getExperience(@RequestParam("id") String id) {
        ExperienceDto experienceDto = userService.getExperienceById(id);
        return ResponseEntity.ok(experienceDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile( HttpServletRequest httpServletRequest) {
        String  id = httpServletRequest.getHeader("X-User-Id");
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User ID missing");
        }
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }









}
