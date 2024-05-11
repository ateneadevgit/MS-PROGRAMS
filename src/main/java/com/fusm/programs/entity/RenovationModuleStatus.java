package com.fusm.programs.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "renovation_module_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RenovationModuleStatus {

    @Id
    @Column(name =  "renovation_module_status_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer renovationModuleStatusId;

    @NonNull
    @Column(name = "status_id", nullable = false)
    private Integer statusId;

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
