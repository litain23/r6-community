package me.r6_search.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.r6_search.service.validate.SignUpValid;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ToString
@Setter
@Getter
@Builder
@SignUpValid
public class SignUpRequestDto {
    @NotBlank
    private String username;

    @NotBlank
    @Email(message = "Email format is incorrect")
    private String email;

    @NotBlank
    private String password;
}
