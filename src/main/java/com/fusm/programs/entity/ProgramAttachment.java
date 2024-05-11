package com.fusm.programs.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "program_attachment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgramAttachment {

    @Id
    @Column(name =  "id_program_attachment", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer programAttachmentId;

    @NonNull
    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @NonNull
    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "order_file", nullable = true)
    private Integer orderFile;

    @NonNull
    @Column(name = "file_type", nullable = false)
    private Integer fileType;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "attachment_father_id", nullable = true)
    private ProgramAttachment attachmentFatherId;

}
