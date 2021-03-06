/*-
 * ========================LICENSE_START=================================
 * TeamApps
 * ---
 * Copyright (C) 2014 - 2020 TeamApps.org
 * ---
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package org.teamapps.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class MultiKeySequentialExecutor<K> {

	private static Logger LOGGER = LoggerFactory.getLogger(MultiKeySequentialExecutor.class);

	private Map<K, SequentialExecutor> sequentialExecutors = new ConcurrentHashMap<>(); // synchronized on submit method level
	private ExecutorService pool;

	public MultiKeySequentialExecutor(int nThreads) {
		pool = Executors.newFixedThreadPool(nThreads);
	}

	public MultiKeySequentialExecutor(ExecutorService executorService) {
		pool = executorService;
	}

	public CompletableFuture<Void> submit(K key, Runnable task) {
		return sequentialExecutors.computeIfAbsent(key, k -> new SequentialExecutor())
				.submit(task);
	}

	public <V> CompletableFuture<V> submit(K key, Supplier<V> task) {
		return sequentialExecutors.computeIfAbsent(key, k -> new SequentialExecutor())
				.submit(task);
	}

	public void closeForKey(K key) {
		sequentialExecutors.compute(key, (k, sequentialExecutor) -> {
			if (sequentialExecutor != null) {
				sequentialExecutor.close();
			}
			return null;
		});
	}

	public SequentialExecutor getExecutorForKey(K key) {
		return sequentialExecutors.get(key);
	}

	public class SequentialExecutor implements Executor {
		private CompletableFuture<?> lastFuture = CompletableFuture.completedFuture(null);
		private AtomicInteger queueSize = new AtomicInteger(0);
		private boolean closed = false;

		@Override
		public void execute(Runnable command) {
			submit(command);
		}

		public synchronized CompletableFuture<Void> submit(Runnable runnable) {
			return this.submit(() -> {
				runnable.run();
				return null;
			});
		}

		public synchronized <V> CompletableFuture<V> submit(Supplier<V> task) {
			if (this.closed) {
				LOGGER.debug("SequentialExecutor already closed.");
				return CompletableFuture.failedFuture(new SequentialExecutorClosedException());
			}
			int queueSize = this.queueSize.incrementAndGet();
			LOGGER.debug("Queue size: {}", queueSize);
			if (queueSize > 500) { // the queue gets quite long when destroying a session, since there are very many listeners to the destroyed event
				LOGGER.warn("Queue is very long: {}", queueSize);
			}
			long submitTime = System.currentTimeMillis();
			CompletableFuture<V> returnedFuture = lastFuture.thenApplyAsync(o -> {
				long executionStartTime = System.currentTimeMillis();
				long delay = executionStartTime - submitTime;
				if (delay > 3000) {
					LOGGER.warn("Execution delay high: {}", delay);
				}
				V result = task.get();
				long executionTime = System.currentTimeMillis() - executionStartTime;
				if (executionTime > 1000) {
					LOGGER.warn("Execution time long: {}", executionTime);
				}
				this.queueSize.decrementAndGet();
				return result;
			}, pool);
			lastFuture = returnedFuture.exceptionally(throwable -> {
				LOGGER.error("Error while executing: ", throwable);
				return null; // do not interrupt the execution chain!!
			});
			return returnedFuture;
		}

		// private since it does not remove itself from the map!
		private synchronized void close() {
			this.closed = true;
		}

	}

	public static class SequentialExecutorClosedException extends RuntimeException {
	}

}
