package com.rishab99058.linkedIn.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("school")
    private String school;

    @JsonProperty("degree")
    private String degree;

    @JsonProperty("field")
    private String field;

    @JsonProperty("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonProperty("end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonProperty("grade")
    private String grade;

    @JsonProperty("description")
    private String description;
}
