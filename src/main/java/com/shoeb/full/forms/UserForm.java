package com.shoeb.full.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserForm {
    @NotBlank
    private String name;
    private String phoneNo;
    @Email(message = "Invalid Email Address.")
    private String email;
    @Size(min = 3 , message = "minimum 3 characters allowed.")
    private String password;
    @NotBlank(message = "Roll No is Required")
    private String rollNo;
    private List<String> roles;
}
