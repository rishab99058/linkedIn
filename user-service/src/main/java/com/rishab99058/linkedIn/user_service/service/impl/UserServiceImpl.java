package com.rishab99058.linkedIn.user_service.service.impl;

import com.cloudinary.Cloudinary;
import com.rishab99058.linkedIn.user_service.dto.EducationDto;
import com.rishab99058.linkedIn.user_service.dto.ExperienceDto;
import com.rishab99058.linkedIn.user_service.dto.UserDto;
import com.rishab99058.linkedIn.user_service.dto.UserStatsDto;
import com.rishab99058.linkedIn.user_service.entity.Experience;
import com.rishab99058.linkedIn.user_service.entity.UserEntity;
import com.rishab99058.linkedIn.user_service.exception.AppCommonException;
import com.rishab99058.linkedIn.user_service.repository.EducationRepository;
import com.rishab99058.linkedIn.user_service.repository.ExperienceRepository;
import com.rishab99058.linkedIn.user_service.repository.UserEntityRepository;
import com.rishab99058.linkedIn.user_service.repository.UserStatRepository;
import com.rishab99058.linkedIn.user_service.service.api.UserService;
import com.rishab99058.linkedIn.user_service.utils.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserEntityRepository userEntityRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final UserStatRepository userStatRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;


    @Override
    public UserDto createUser(UserDto userDto, MultipartFile profilePictureFile, MultipartFile coverPictureFile) {
        try {
            if(profilePictureFile!= null) {
              userDto.setProfilePictureUrl(cloudinaryService.uploadFile(profilePictureFile));
            }
            if(coverPictureFile != null) {
                userDto.setCoverPictureUrl(cloudinaryService.uploadFile(coverPictureFile));
            }
            UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
            userEntity.setCreatedAt(LocalDateTime.now());
            userEntity.setUpdatedAt(LocalDateTime.now());
            userEntity = userEntityRepository.save(userEntity);
            return modelMapper.map(userEntity, UserDto.class);
        } catch (Exception e) {
            log.error("Error in createUser: {}", e.getMessage(), e);
            throw new AppCommonException("User creation failed");
        }
    }

    @Override
    public UserDto getUserById(String id) {
        try {
            UserEntity userEntity = userEntityRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new AppCommonException("User not found with id: " + id));
            UserDto userDto = modelMapper.map(userEntity, UserDto.class);
            if (userEntity.getExperiences() != null) {
                Set<ExperienceDto> experiences = userEntity.getExperiences()
                        .stream()
                        .map(exp -> modelMapper.map(exp, ExperienceDto.class))
                        .collect(Collectors.toSet());
                userDto.setExperiences(experiences);
            }
            if (userEntity.getEducations() != null) {
                Set<EducationDto> educations = userEntity.getEducations()
                        .stream()
                        .map(edu -> modelMapper.map(edu, EducationDto.class))
                        .collect(Collectors.toSet());
                userDto.setEducations(educations);
            }
            return userDto;
        } catch (AppCommonException e) {
            log.warn("getUserById exception: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error in getUserById: {}", e.getMessage(), e);
            throw new AppCommonException("Error retrieving user details");
        }
    }


    @Override
    public List<UserDto> getAllUsers(int page, int size, String filter) {
        try {
            // Sort by firstName ASC
            Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());

            Page<UserEntity> userEntities;
            if (filter == null || filter.trim().isEmpty()) {
                userEntities = userEntityRepository.findAllByDeletedFalseAndDeactivatedFalse(pageable);
            } else {
                userEntities = userEntityRepository.searchAllFlexible(filter.trim(), pageable);
            }

            return userEntities.stream()
                    .map(userEntity -> modelMapper.map(userEntity, UserDto.class))
                    .toList();
        } catch (Exception e) {
            log.error("Error in getAllUsers: {}", e.getMessage(), e);
            return List.of();
        }
    }


    @Override
    public UserDto updateUser(String id, UserDto userDto, MultipartFile profilePictureFile, MultipartFile coverPictureFile) {
        try {
            UserEntity user = userEntityRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new AppCommonException("User not found"));
            if(user.isDeactivated() == true)
                throw new AppCommonException("User has been deactivated");
            if(userDto.getFirstName() != null) user.setFirstName(userDto.getFirstName());
            if(userDto.getLastName() != null) user.setLastName(userDto.getLastName());
            if(userDto.getHeadline() != null) user.setHeadline(userDto.getHeadline());
            if(userDto.getAbout() != null) user.setAbout(userDto.getAbout());
            if(userDto.getPhoneNumber() != null) user.setPhoneNumber(userDto.getPhoneNumber());
            if(userDto.getCountry() != null) user.setCountry(userDto.getCountry());
            if(userDto.getState() != null) user.setState(userDto.getState());
            if(userDto.getCity() != null) user.setCity(userDto.getCity());
            if(userDto.getAddress() != null) user.setAddress(userDto.getAddress());
            if(userDto.getIndustry() != null) user.setIndustry(userDto.getIndustry());
            if(userDto.getCurrentCompany() != null) user.setCurrentCompany(userDto.getCurrentCompany());
            if(userDto.getCurrentPosition() != null) user.setCurrentPosition(userDto.getCurrentPosition());
            if(userDto.getWebSiteLink() != null) user.setWebSiteLink(userDto.getWebSiteLink());
            if(userDto.getSkills() != null) user.setSkills(new HashSet<>(userDto.getSkills()));
            if(userDto.getLanguages() != null) user.setLanguages(new HashSet<>(userDto.getLanguages()));
            if(userDto.getCertifications() != null) user.setCertifications(new HashSet<>(userDto.getCertifications()));
            if(userDto.getProjects() != null) user.setProjects(new HashSet<>(userDto.getProjects()));
            if(userDto.getAccomplishments() != null) user.setAccomplishments(new HashSet<>(userDto.getAccomplishments()));
            if (profilePictureFile != null && !profilePictureFile.isEmpty()) {
                String url = cloudinaryService.uploadFile(profilePictureFile);
                user.setProfilePictureUrl(url);
            }
            if (coverPictureFile != null && !coverPictureFile.isEmpty()) {
                String url = cloudinaryService.uploadFile(coverPictureFile);
                user.setCoverPictureUrl(url);
            }
            user.setUpdatedAt(LocalDateTime.now());
            userEntityRepository.save(user);
            return modelMapper.map(user, UserDto.class);
        } catch (AppCommonException e) {
            log.warn("updateUser error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error in updateUser: {}", e.getMessage(), e);
            throw new AppCommonException("User update failed: " + e.getMessage());
        }
    }


    @Override
    public void softDeleteUser(String id) {
        try {
            UserEntity user = userEntityRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new AppCommonException("User not found"));
            user.setDeleted(true);
            user.setUpdatedAt(LocalDateTime.now());
            userEntityRepository.save(user);
        } catch (Exception e) {
            log.error("Error in softDeleteUser: {}", e.getMessage(), e);
            throw new AppCommonException("User could not be soft deleted: " + e.getMessage());
        }
    }


    @Override
    public void deactivateUser(String id) {
        try {
            UserEntity user = userEntityRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new AppCommonException("User not found"));
            user.setDeactivated(true);
            user.setUpdatedAt(LocalDateTime.now());
            userEntityRepository.save(user);
        } catch (Exception e) {
            log.error("Error in deactivateUser: {}", e.getMessage(), e);
            throw new AppCommonException("User could not be deactivated: " + e.getMessage());
        }
    }

    @Override
    public void activateUser(String id) {
        try {
            UserEntity user = userEntityRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new AppCommonException("User not found"));
            user.setDeactivated(false);
            user.setUpdatedAt(LocalDateTime.now());
            userEntityRepository.save(user);
        } catch (Exception e) {
            log.error("Error in activateUser: {}", e.getMessage(), e);
            throw new AppCommonException("User could not be activated: " + e.getMessage());
        }
    }

    @Override
    public void updateProfilePicture(String id, MultipartFile profilePictureFile) {
        try {
            UserEntity user = userEntityRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new AppCommonException("User not found"));
            if(user.isDeactivated() == true)
                throw new AppCommonException("User has been deactivated");
            if (profilePictureFile != null && !profilePictureFile.isEmpty()) {
                String url = cloudinaryService.uploadFile(profilePictureFile);
                user.setProfilePictureUrl(url);
                user.setUpdatedAt(LocalDateTime.now());
                userEntityRepository.save(user);
            }
        } catch (Exception e) {
            log.error("Error in updateProfilePicture: {}", e.getMessage(), e);
            throw new AppCommonException("Profile picture update failed: " + e.getMessage());
        }
    }

    @Override
    public void updateCoverPicture(String id, MultipartFile coverPictureFile) {
        try {
            UserEntity user = userEntityRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new AppCommonException("User not found"));
            if(user.isDeactivated() == true)
                throw new AppCommonException("User has been deactivated");
            if (coverPictureFile != null && !coverPictureFile.isEmpty()) {
                String url = cloudinaryService.uploadFile(coverPictureFile);
                user.setCoverPictureUrl(url);
                user.setUpdatedAt(LocalDateTime.now());
                userEntityRepository.save(user);
            }
        } catch (Exception e) {
            log.error("Error in updateCoverPicture: {}", e.getMessage(), e);
            throw new AppCommonException("Cover picture update failed: " + e.getMessage());
        }
    }


    @Override
    public List<ExperienceDto> getExperiences(String userId) {
        try {
            // Validate userId
            if (userId == null || userId.trim().isEmpty()) {
                throw new AppCommonException("User ID cannot be null or empty");
            }

            if (!userEntityRepository.existsByIdAndDeletedFalseAndDeactivatedFalse(userId)) {
                throw new AppCommonException("User not found with id: " + userId);
            }

            // Fetch experiences directly by userId
            List<Experience> experiences = experienceRepository.findByUserId(userId);
            if (experiences == null || experiences.isEmpty()) {
                return List.of();
            }

            return experiences.stream()
                    .map(exp -> modelMapper.map(exp, ExperienceDto.class))
                    .toList();
        } catch (Exception e) {
            log.warn("getExperiences validation error: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public ExperienceDto getExperienceById(String id) {
        try {

            Experience experience = experienceRepository.
                    findById(id).orElseThrow(() ->
                            new AppCommonException("Experience Document not found"));
            return modelMapper.map(experience, ExperienceDto.class);

        }catch (Exception e) {
            log.warn("getExperiences validation error: {}", e.getMessage());
            throw e;
        }
    }


    @Override
    public ExperienceDto addExperience(String userId, ExperienceDto expDto) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                throw new AppCommonException("User ID cannot be null or empty");
            }
            if (expDto == null) {
                throw new AppCommonException("Experience data cannot be null");
            }
            if (!userEntityRepository.existsByIdAndDeletedFalseAndDeactivatedFalse(userId)) {
                throw new AppCommonException("User not found with id: " + userId);
            }
            Experience experience = modelMapper.map(expDto, Experience.class);
            experience.setUserId(userId);

            Experience savedExperience = experienceRepository.save(experience);

            return modelMapper.map(savedExperience, ExperienceDto.class);

        } catch (AppCommonException e) {
            log.warn("addExperience validation error: {}", e.getMessage());
            throw e;
        }
    }


    @Override
    public ExperienceDto updateExperience(String userId, String expId, ExperienceDto expDto) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                throw new AppCommonException("User ID cannot be null or empty");
            }
            if (expId == null || expId.trim().isEmpty()) {
                throw new AppCommonException("Experience ID cannot be null or empty");
            }
            if (expDto == null) {
                throw new AppCommonException("Experience data cannot be null");
            }

            if (!userEntityRepository.existsByIdAndDeletedFalseAndDeactivatedFalse(userId)) {
                throw new AppCommonException("User not found with id: " + userId);
            }
            Experience existingExperience = experienceRepository.findById(expId)
                    .orElseThrow(() -> new AppCommonException("Experience not found with id: " + expId));
            if (!existingExperience.getUserId().equals(userId)) {
                throw new AppCommonException("Experience does not belong to the specified user");
            }
            if (expDto.getTitle() != null) {
                existingExperience.setTitle(expDto.getTitle());
            }
            if (expDto.getCompany() != null) {
                existingExperience.setCompany(expDto.getCompany());
            }
            if (expDto.getLocation() != null) {
                existingExperience.setLocation(expDto.getLocation());
            }
            if (expDto.getStartDate() != null) {
                existingExperience.setStartDate(expDto.getStartDate());
            }
            if (expDto.getEndDate() != null) {
                existingExperience.setEndDate(expDto.getEndDate());
            }
            if (expDto.getCurrent() != null) {
                existingExperience.setCurrent(expDto.getCurrent());
            }
            if (expDto.getDescription() != null) {
                existingExperience.setDescription(expDto.getDescription());
            }
            Experience updatedExperience = experienceRepository.save(existingExperience);
            return modelMapper.map(updatedExperience, ExperienceDto.class);

        }catch (Exception e) {
            log.error("Error in updateExperience: {}", e.getMessage(), e);
            throw new AppCommonException("Failed to update experience: " + e.getMessage());
        }

    }

    @Override
    public void deleteExperience(String userId, String expId) {
        try {

            if (userId == null || userId.trim().isEmpty()) {
                throw new AppCommonException("User ID cannot be null or empty");
            }
            if (expId == null || expId.trim().isEmpty()) {
                throw new AppCommonException("Experience ID cannot be null or empty");
            }

            if (!userEntityRepository.existsByIdAndDeletedFalseAndDeactivatedFalse(userId)) {
                throw new AppCommonException("User not found with id: " + userId);
            }

            Experience experience = experienceRepository.findById(expId)
                    .orElseThrow(() -> new AppCommonException("Experience not found with id: " + expId));

            if (!experience.getUserId().equals(userId)) {
                throw new AppCommonException("Experience does not belong to the specified user");
            }
            experienceRepository.delete(experience);

            log.info("Experience deleted successfully: userId={}, expId={}", userId, expId);

        } catch (Exception e) {
            log.error("Error in deleteExperience: {}", e.getMessage(), e);
            throw new AppCommonException("Failed to delete experience: " + e.getMessage());
        }
    }

    @Override
    public List<EducationDto> getEducations(String userId) {
        return List.of();
    }

    @Override
    public EducationDto addEducation(String userId, EducationDto eduDto) {
        return null;
    }

    @Override
    public EducationDto updateEducation(String userId, String eduId, EducationDto eduDto) {
        return null;
    }

    @Override
    public EducationDto getEducationById(String id) {
        return null;
    }

    @Override
    public void deleteEducation(String userId, String eduId) {

    }

    @Override
    public void updateSkills(String userId, List<String> skills) {

    }

    @Override
    public void updateLanguages(String userId, List<String> languages) {

    }

    @Override
    public void updateCertifications(String userId, List<String> certifications) {

    }

    @Override
    public void updateProjects(String userId, List<String> projects) {

    }

    @Override
    public void updateAccomplishments(String userId, List<String> accomplishments) {

    }

    @Override
    public UserStatsDto getUserStats(String userId) {
        return null;
    }
}
