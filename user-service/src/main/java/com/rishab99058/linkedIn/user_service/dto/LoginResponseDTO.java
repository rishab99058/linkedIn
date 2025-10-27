package com.rishab99058.linkedIn.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    @JsonProperty("jwt_token")
    private String jwtToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("user_details")
    private UserDto userDto;
}
