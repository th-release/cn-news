package com.coinnews.responses;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NewsResponse {
    private List<String> originCodes;

    private long id;

    private long categoryId;

    private String title;

    private String content;

    private String thumbnailImage;

    private String contentImage;

    private String link;

    private String linkTitle;

    private long bull;

    private long bear;

    private boolean isBull;

    private boolean isImportant;

    private String publishAt;

    private long quoteCount;

    private boolean isObserve;
}
