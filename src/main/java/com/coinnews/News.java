package com.coinnews;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.coinnews.responses.NewsResponse;
import com.squareup.okhttp.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class News {
    private static NewsResponse lastNews = NewsResponse.builder()
            .id(0)
            .build();

    public void execute() {
        OkHttpClient client = new OkHttpClient();
        Dotenv dotenv = Dotenv.configure().load();

        try {
            Request newsRequest = new Request.Builder()
                    .url("https://api.coinness.com/feed/v1/breaking-news")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();

            Response newsResponse = client.newCall(newsRequest).execute();
            Gson gson = new Gson();
            NewsResponse[] newsArray = gson.fromJson(newsResponse.body().string(), NewsResponse[].class);
            List<NewsResponse> news = Arrays.stream(newsArray).toList();

            if (newsResponse.isSuccessful() &&
                lastNews.getId() != news.get(0).getId() &&
                !news.get(0).getTitle().equals("코인니스 뉴스 제공 시간 안내") &&
                !news.get(0).getTitle().equals("코인니스 커뮤니티 주간 인기글")
            ) {
                String content = "# " + news.get(0).getTitle() + "\n> " + news.get(0).getContent();

                if (!news.get(0).getOriginCodes().isEmpty()) {
                    StringBuilder joinString = new StringBuilder();
                    for (String item : news.get(0).getOriginCodes()) {
                        joinString.append(item).append(",");
                    }
                    joinString.deleteCharAt(joinString.length() - 1);

                    content += "\n\n> 관련 주식 혹은 코인: " + joinString.toString();
                }

                if (news.get(0).getContentImage() != null) {
                    content += "\n" + news.get(0).getContentImage();
                }
                content = content.replace("\"", "\\\""); // 따옴표를 이스케이프 처리

                JsonObject jsonBody = new JsonObject();

                jsonBody.addProperty("content", content);
                jsonBody.addProperty("username", "Batis News");
                jsonBody.addProperty("avatar_url", "https://cdn.discordapp.com/avatars/916501793884741705/bea48c5cd0589518f003fe62f24c6305.webp?size=64");

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, jsonBody.toString());

                Request request = new Request.Builder()
                        .url("https://discord.com/api/v10/webhooks/" + dotenv.get("WEB_HOOK_ID") + "?wait=true")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    System.out.println("Error Response: " + response.body().string());
                }

                lastNews = news.get(0);
            }
        } catch (IOException | NullPointerException e) {
            System.out.println("error e");
            System.out.println(e.getMessage());
        }
    }
}
