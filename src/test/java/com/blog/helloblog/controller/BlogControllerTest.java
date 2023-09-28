package com.blog.helloblog.controller;

import com.blog.helloblog.domain.Article;
import com.blog.helloblog.dto.request.AddArticleRepuset;
import com.blog.helloblog.repository.BlogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class BlogControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 클래스

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void setMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();;
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // given - 블로그 글 추가에 필요한 요청 객체를 만든다.
        final String url = "/api/articles";
        final String title = "title";
        final String content = "centent";
        final AddArticleRepuset repuset = new AddArticleRepuset(title, content);

        // 객체 JSON으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(repuset);

        // when - 블로그 글 추가 API에 요청을 보낸다. 이떄 요청 타입은 JSON이며 given절에 미리 만든어둔 객체를 요청 본문으로 함께 보냄
        // 설정한 내용을 바탕으로 요청 전송
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then - 응답 코드가 201 Created인지 확인, Blog를 전체 조회해 크기가 1인지 확인 후 실제로 저장된 데이터의 요청 값을 비교
        // 응답 코드가 201 인지 검사
        result
                .andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        Assertions.assertThat(articles.size()).isEqualTo(1);
        Assertions.assertThat(articles.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception {
        // given - 블로그 글 저장
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder()
                        .title(title)
                        .content(content)
                        .build());

        // when - 목록 조회 API를 호출
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then - 응답 코드가 200인지, 반환값 중 0번째 요소의 content와 title값이 저장된 값과 같은지 확인
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));
    }
}