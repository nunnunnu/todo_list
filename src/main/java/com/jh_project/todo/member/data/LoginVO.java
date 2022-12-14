package com.jh_project.todo.member.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {
    private String email;
    private String pwd;    
}
