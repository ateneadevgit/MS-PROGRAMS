package com.fusm.programs.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@Table(name = "proposal_renovation_modules")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProposalRenovationModules {

    @Id
    @Column(name =  "id_renovation_module", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer renovationModuleId;

    @ManyToOne
    @JoinColumn(name = "proposal_renovation_id", nullable = false)
    private ProposalRenovation proposalRenovationId;

    @ManyToOne
    @JoinColumn(name = "program_module_id", nullable = false)
    private ProgramModule programModuleId;

    @NonNull
    @Column(name = "is_editable", nullable = false)
    private Boolean isEditable;

}
