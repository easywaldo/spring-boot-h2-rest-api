package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.dto.member.ValidMemberRequestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    public void given_valid_member_user_id_and_password_then_is_valid_user_should_return_true() {
        // arrange
        ValidMemberRequestDto requestDto = ValidMemberRequestDto.builder()
            .userId("easywaldo")
            .userPwd("1234")
            .build();

        // act
        var isValid = memberService.validUser(requestDto);

        // assert
        assertThat(isValid).isEqualTo(true);
    }

    @Test
    public void given_un_valid_member_user_id_and_password_then_is_valid_user_should_return_false() {
        // arrange
        ValidMemberRequestDto requestDto = ValidMemberRequestDto.builder()
            .userId("easywaldo")
            .userPwd("1111")
            .build();

        // act
        var isValid = memberService.validUser(requestDto);

        // assert
        assertThat(isValid).isEqualTo(false);
    }
}
