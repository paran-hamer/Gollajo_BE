package com.gollajo.domain.test.controller;

import com.gollajo.domain.test.service.TestService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(TestControllerTest.class)
class TestControllerTest {

    @MockBean
    TestService testService;

    @BeforeEach
    void setUp(){
        RestAssuredMockMvc.standaloneSetup(new TestController(testService));
    }

    @Test
    @DisplayName("testAPI를 실행하여 성공메세지를 받는다")
    void test(){
        //given
        //when&then
        RestAssuredMockMvc.given().log().all()
                .when().get("/test")
                .then().log().all()
                .status(HttpStatus.OK);

    }
}