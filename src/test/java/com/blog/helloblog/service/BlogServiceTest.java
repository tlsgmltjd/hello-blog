package com.blog.helloblog.service;

import com.blog.helloblog.domain.Article;
import com.blog.helloblog.dto.request.AddArticleRepuset;
import com.blog.helloblog.repository.BlogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class BlogServiceTest {
    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogRepository blogRepository;

    @Test
    void 글저장비즈니스로직() {
        // given - 블로그 객체 저장
        String title = "title";
        String content = "content";

        final AddArticleRepuset request = new AddArticleRepuset(title, content);

        // when - 블로그 글 저장
        Article savedArticle = blogService.save(request);

        // then - 가져온 글과 저장했던 글이 같은지 확인
        Article article = blogRepository.findById(savedArticle.getId())
                .orElseThrow(() -> new NullPointerException("글을 찾을 수 없음"));

        Assertions.assertThat(article.getTitle()).isEqualTo(title);
        Assertions.assertThat(article.getContent()).isEqualTo(content);
    }
}