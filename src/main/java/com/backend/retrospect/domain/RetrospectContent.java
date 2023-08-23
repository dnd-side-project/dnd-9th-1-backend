package com.backend.retrospect.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "retrospect_content")
public class RetrospectContent {
    @Id @GeneratedValue
    private Long id;

    @Enumerated
    @Column(name = "guide")
    private Guide guide;

    @Column(name = "content", length = 1000)
    private String content;

    public RetrospectContent(Guide guide, String content){
        this.guide = guide;
        this.content = content;
    }
}
