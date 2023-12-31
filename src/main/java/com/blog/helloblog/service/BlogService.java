package com.blog.helloblog.service;

import com.blog.helloblog.domain.Article;
import com.blog.helloblog.dto.request.AddArticleRepuset;
import com.blog.helloblog.dto.request.RequestUpdateArticle;
import com.blog.helloblog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRepuset repuset) {
        return blogRepository.save(repuset.toEntity());
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public void delete(Long id) {
        blogRepository.deleteById(id);
    }

    @Transactional // 트랜잭션으로 묶는 역할을함 (데이터를 바꾸기 위한 최소 작업 단위)
    public Article update(Long id, RequestUpdateArticle result) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        article.update(result.getTitle(), result.getContent());

        return article;
    }
}
