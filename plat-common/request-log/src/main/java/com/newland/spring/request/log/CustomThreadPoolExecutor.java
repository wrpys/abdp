package com.newland.spring.request.log;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


@Configuration
public class CustomThreadPoolExecutor {
	@Value("${thread.pool.core:10}")
	private int corePool;
	@Value("${thread.pool.max:10}")
	private int maxPool;

	@Value("${thread.queue.capacity:10}")
	private int queueCapacity;

	private ThreadPoolExecutor pool = null;

	@Bean
	public ThreadPoolExecutor init() {
		pool = new ThreadPoolExecutor(
				corePool,
				maxPool,
				30,
				TimeUnit.MINUTES,
				new ArrayBlockingQueue<Runnable>(queueCapacity),
				new CustomThreadFactory(),
				new CustomRejectedExecutionHandler());
		return pool;
	}


	public void destory() {
		if (pool != null) {
			pool.shutdownNow();
		}
	}

	public ExecutorService getCustomThreadPoolExecutor() {
		return this.pool;
	}

	private class CustomThreadFactory implements ThreadFactory {

		private AtomicInteger count = new AtomicInteger(0);

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			String threadName = CustomThreadPoolExecutor.class.getSimpleName() + count.addAndGet(1);
//			System.out.println(threadName);
			t.setName(threadName);
			return t;
		}
	}


	private class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			try {
				// 核心改造点，由blockingqueue的offer改成put阻塞方法
				executor.getQueue().put(r);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 记录异常
			// 报警处理等
//			System.out.println("error.............");
		}
	}
}
