package com.project.dto.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponseDto {
    private Long userId;
    private String fullName;
    private String email;
    private Integer projectSize;
    private String token;
}