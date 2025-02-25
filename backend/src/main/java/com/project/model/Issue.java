package com.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "issue")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ISSUE_ID")
    private Long issueId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false)
    private User assignee;

}