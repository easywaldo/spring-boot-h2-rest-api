package com.approval.document.documentapproval.domain.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SHAEncryptServiceImplTest {
    @Test
    public void given_user_pass_word_then_get_sha_512_should_returns_hashed_data_() {
        // arrange
        String userPassword = "1234";

        // act
        String hashedPwd = SHAEncryptServiceImpl.getSHA512(userPassword);

        // assert
        assertThat(hashedPwd).isEqualTo(SHAEncryptServiceImpl.getSHA512(userPassword));

    }
}
