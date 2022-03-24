package com.icia.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginDTO {
    @NotBlank(message = "이메일 필수지용")
    private String memberEmail;
    @NotBlank
    @Length(min = 2, max = 8, message = "2~8자의 비밀번호를 입력하세요")
    private String memberPassword;

}
