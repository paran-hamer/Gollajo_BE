package com.gollajo.domain.test.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TestServiceTest {

    @Autowired
    TestService testService;

    @Test
    @DisplayName("test를 진행합니다")
    void test(){
        //given

        //when
        String testMessage = testService.test();
        //then
        assertThat(testMessage).isEqualTo("test Success");
    }

}