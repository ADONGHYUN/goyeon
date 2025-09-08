package com.dh.goyeon.user.impl;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("mail-executor-");
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		// TODO Auto-generated method stub
		return new CustomAsyncExceptionHandler();
	}

	public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
		@Override
		public void handleUncaughtException(Throwable ex, Method method, Object... params) {
			System.err.println("에러메시지: " + ex.getMessage());
			System.err.println("메소드명: " + method.getName());
			for (Object param : params) {
				System.err.println("파라미터값: " + param);
			}
		}
	}
}
