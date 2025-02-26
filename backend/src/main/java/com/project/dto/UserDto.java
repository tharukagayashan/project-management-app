package com.project.dto;

import com.project.model.Issue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Long userId;
    private String fullName;
    private String email;
    private String password;
    private Integer projectSize;

    private List<IssueDto> assignedIssues = new ArrayList<>();
}
