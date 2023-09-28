package com.blog.helloblog.controller;

import com.blog.helloblog.domain.Article;
import com.blog.helloblog.dto.request.AddArticleRepuset;
import com.blog.helloblog.dto.response.ArticleReponse;
import com.blog.helloblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRepuset repuset) {
        Article savedArticle = blogService.save(repuset);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleReponse>> findAllArticles() {
        List<ArticleReponse> articles = blogService.findAll()
                .stream()
                .map(ArticleReponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }
}
