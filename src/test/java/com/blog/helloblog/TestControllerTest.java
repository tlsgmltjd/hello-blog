package com.blog.helloblog;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 테스트용 애플리케이션 컨텍스트 생성
@AutoConfigureMockMvc // MockMvc 생성
class TestControllerTest {

     @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @AfterEach
    public void cleanUp() {
        memberRepository.deleteAll();
    }

    @DisplayName("getAllMembers: 아트클 조회에 성공한다.")
    @Test
    public void getAllMembers() throws Exception {
        // given 맴버를 저장
        final String url = "/test";
        Member savesMember = memberRepository.save(new Member(1L, "신희성"));

        // when 맴버 리스트를 조회하는 API를 호출
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE));

        // then 응답 코드가 200이고 반환 값 중에 0번째 요소의 id,name이 저장된 값과 일치하는지 확인
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(savesMember.getId()))
                .andExpect(jsonPath("$[0].name").value(savesMember.getName()));
    }
    

}