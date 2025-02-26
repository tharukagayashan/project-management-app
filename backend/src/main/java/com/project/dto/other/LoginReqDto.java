package com.project.dto.other;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginReqDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}