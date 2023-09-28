package com.blog.helloblog.dto.response;

import com.blog.helloblog.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ArticleReponse {
    private final String title;
    private final String content;

    public ArticleReponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
