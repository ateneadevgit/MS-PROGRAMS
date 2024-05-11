package com.fusm.programs.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "second_language")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SecondLanguage {

    @Id
    @Column(name =  "id_second_language", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer secondLanguageId;

    @NonNull
    @Column(name = "cover", length = 300, nullable = false)
    private String cover;

    @NonNull
    @Column(name = "tittle", length = 200, nullable = false)
    private String tittle;

    @NonNull
    @Column(name = "start_level", length = 5, nullable = false)
    private String startLevel;

    @NonNull
    @Column(name = "end_level", length = 5, nullable = false)
    private String endLevel;

    @NonNull
    @Column(name = "group_id", nullable = false)
    private Integer groupId;

    @NonNull
    @Column(name = "icon", length = 300, nullable = false)
    private String icon;

    @NonNull
    @Column(name = "descriprion", length = 300, nullable = false)
    private String description;

    @NonNull
    @Column(name = "modality_id", nullable = false)
    private Integer modalityId;

    @NonNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NonNull
    @Column(name = "hours", nullable = false)
    private Integer hours;

    @NonNull
    @Column(name = "inscription_link", length = 300, nullable = false)
    private String inscriptionLink;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

}
