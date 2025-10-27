package com.rishab99058.linkedIn.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaseDTO {
    @JsonProperty("message")
    private String message;
}
