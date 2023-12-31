package com.blog.helloblog.controller;

import com.blog.helloblog.domain.Article;
import com.blog.helloblog.dto.request.AddArticleRepuset;
import com.blog.helloblog.dto.request.RequestUpdateArticle;
import com.blog.helloblog.dto.response.ArticleReponse;
import com.blog.helloblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRepuset repuset) {
        Article savedArticle = blogService.save(repuset);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleReponse>> findAllArticles() {
        List<ArticleReponse> articles = blogService.findAll()
                .stream()
                .map(ArticleReponse::new)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(articles);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleReponse> findArticle(@PathVariable Long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(new ArticleReponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        blogService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id,
                                                 @RequestBody RequestUpdateArticle request) {
        Article updatedArticle = blogService.update(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(updatedArticle);
    }
}
