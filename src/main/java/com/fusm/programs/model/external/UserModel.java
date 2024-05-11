package com.fusm.programs.model.external;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private String email;
    private String password;
    private String name;
    private Integer faculty;
    private Integer role;

}

