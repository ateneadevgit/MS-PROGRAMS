package com.fusm.programs.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "review")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @Column(name =  "id_review", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @NonNull
    @Column(name = "review", length = 1000, nullable = false)
    private String review;

    @NonNull
    @Column(name = "object_type", nullable = false)
    private Integer objectType;

    @NonNull
    @Column(name = "object_id", nullable = false)
    private Integer objectId;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "roleId", nullable = false)
    private Integer roleId;

    @ManyToOne
    @JoinColumn(name = "reply_id", nullable = true)
    private Review replyTo;

}
