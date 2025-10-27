package com.rishab99058.linkedIn.user_service.service.api;

import com.rishab99058.linkedIn.user_service.dto.EducationDto;
import com.rishab99058.linkedIn.user_service.dto.ExperienceDto;
import com.rishab99058.linkedIn.user_service.dto.UserDto;
import com.rishab99058.linkedIn.user_service.dto.UserStatsDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    // Basic Profile
    UserDto getUserById(String id);
    List<UserDto> getAllUsers(int page, int size, String filter); // Pagination + filtering (optional)
    UserDto updateUser(String id, UserDto userDto, MultipartFile profilePictureFile, MultipartFile coverPictureFile);
    void softDeleteUser(String id);
    void deactivateUser(String id);
    void activateUser(String id);

    // profile Picture
    void updateProfilePicture(String id, MultipartFile profilePictureFile);
    void updateCoverPicture(String id, MultipartFile coverPictureFile);

    // EXPERIENCE
    List<ExperienceDto> getExperiences(String userId);
    ExperienceDto getExperienceById(String id);
    ExperienceDto addExperience(String userId, ExperienceDto expDto);
    ExperienceDto updateExperience(String userId, String expId, ExperienceDto expDto);
    void deleteExperience(String userId, String expId);

    // EDUCATION
    List<EducationDto> getEducations(String userId);
    EducationDto addEducation(String userId, EducationDto eduDto);
    EducationDto updateEducation(String userId, String eduId, EducationDto eduDto);
    EducationDto getEducationById(String id);
    void deleteEducation(String userId, String eduId);

    // SKILLS, LANGUAGES, CERTIFICATIONS, PROJECTS, ACCOMPLISHMENTS
    void updateSkills(String userId, List<String> skills);
    void updateLanguages(String userId, List<String> languages);
    void updateCertifications(String userId, List<String> certifications);
    void updateProjects(String userId, List<String> projects);
    void updateAccomplishments(String userId, List<String> accomplishments);

    // USER STATS
    UserStatsDto getUserStats(String userId);



}
