package com.fusm.programs.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "object_temp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ObjectTemp {

    @Id
    @Column(name =  "object_temp_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer objectTempId;

    @NonNull
    @Column(name = "object_value", length = 1000, nullable = false)
    private String objectValue;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "proposal_renovation_module_id", nullable = false)
    private ProposalRenovationModules proposalRenovationModulesId;

}
