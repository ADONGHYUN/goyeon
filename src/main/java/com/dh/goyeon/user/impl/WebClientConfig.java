package com.dh.goyeon.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
   
    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 연결 타임아웃
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(10)) // 읽기 타임아웃 (초)
                        .addHandlerLast(new WriteTimeoutHandler(10))); // 쓰기 타임아웃 (초)
    }
    
    @Bean
    @Autowired
    public WebClient.Builder webClientBuilder(HttpClient httpClient) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
