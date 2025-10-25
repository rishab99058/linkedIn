package com.rishab99058.linkedIn.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import jakarta.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("first_name")
    @NotBlank(message = "first_name is required")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "last_name is required")
    private String lastName;

    @JsonProperty("email")
    @Email(message = "invalid email format")
    @NotBlank(message = "email is required")
    private String email;

    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "password is required")
    private String password;

    @JsonProperty("profile_picture_url")
    private String profilePictureUrl;

    @JsonProperty("cover_picture_url")
    private String coverPictureUrl; // Don't forget to add this to entity if you need

    @JsonProperty("headline")
    private String headline;

    @JsonProperty("about")
    private String about;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("country")
    private String country;

    @JsonProperty("state")
    private String state;

    @JsonProperty("city")
    private String city;

    @JsonProperty("address")
    private String address;

    @JsonProperty("industry")
    private String industry;

    @JsonProperty("current_company")
    private String currentCompany;

    @JsonProperty("current_position")
    private String currentPosition;

    @JsonProperty("web_site_link")
    private String webSiteLink;

    @JsonProperty("skills")
    private Set<String> skills;

    @JsonProperty("languages")
    private Set<String> languages;

    @JsonProperty("experiences")
    private Set<ExperienceDto> experiences;

    @JsonProperty("educations")
    private Set<EducationDto> educations;

    @JsonProperty("certifications")
    private Set<String> certifications;

    @JsonProperty("projects")
    private Set<String> projects;

    @JsonProperty("accomplishments")
    private Set<String> accomplishments;

    @JsonProperty("deleted")
    private boolean deleted;

    @JsonProperty("deactivated")
    private boolean deactivated;

    @JsonProperty("created_at")
    private String createdAt; // ISO string or convert as required

    @JsonProperty("updated_at")
    private String updatedAt;
}
