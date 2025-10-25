package com.rishab99058.linkedIn.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatsDto {

    @JsonProperty("connections_count")
    private int connectionsCount;

    @JsonProperty("followers_count")
    private int followersCount;

    @JsonProperty("posts_count")
    private int postsCount;

    @JsonProperty("experiences_count")
    private int experiencesCount;

    @JsonProperty("educations_count")
    private int educationsCount;

    @JsonProperty("skills_count")
    private int skillsCount;

    @JsonProperty("projects_count")
    private int projectsCount;

    @JsonProperty("certifications_count")
    private int certificationsCount;

    @JsonProperty("accomplishments_count")
    private int accomplishmentsCount;

    // Add more if you track endorsements, languages, etc.

    @JsonProperty("profile_completion_percent")
    private int profileCompletionPercent; // Calculated if you want to show profile completeness
}
