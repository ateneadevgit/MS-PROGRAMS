package com.fusm.programs.model.external;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Template {

    private Integer templateId;
    private String templateName;
    private String description;
    private String subject;
    private String emailBody;

}
