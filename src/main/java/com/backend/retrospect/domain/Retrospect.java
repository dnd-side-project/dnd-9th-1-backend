package com.backend.retrospect.domain;

import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "retrospect")
public class Retrospect extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retrospect_id")
    private Long id;

    @Column(name = "goal_id")
    private Long goalId;

    @Column(name = "has_guide")
    private Boolean hasGuide;

    @OneToMany(mappedBy = "retrospect", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RetrospectContent> contents;

    @Enumerated(EnumType.STRING)
    @Column(name = "success_level")
    private SuccessLevel successLevel;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @PrePersist
    private void init(){
        isDeleted = false;
    }

    public Retrospect(Long goalId, Boolean hasGuide, Map<Guide, String> contents, SuccessLevel successLevel){
        this.goalId = goalId;
        this.hasGuide = hasGuide;
        this.successLevel = successLevel;

        List<RetrospectContent> retrospectContents = new ArrayList<>();
        for(Map.Entry<Guide, String> entry : contents.entrySet()){
            Guide guide = entry.getKey();
            String content = entry.getValue();

            retrospectContents.add(new RetrospectContent(guide, content, this));
        }
        this.contents = retrospectContents;
    }
}
