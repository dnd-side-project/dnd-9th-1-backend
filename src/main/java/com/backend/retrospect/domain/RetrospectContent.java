package com.backend.retrospect.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "retrospect_content")
public class RetrospectContent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retrospect_content_id")
    private Long id;

    @Enumerated
    @Column(name = "guide")
    private Guide guide;

    @Column(name = "content", length = 1000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "retrospect_id")
    private Retrospect retrospect;

    public RetrospectContent(Guide guide, String content){
        this.guide = guide;
        this.content = content;
    }
}
