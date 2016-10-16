package io.korhner.uuid_benchmark;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

/**
 * Executor without queue overhead.
 */
public class LockFreeExecutor {

	private final int workers;
	private final int numPerWorker;
	private final Runnable runnable;
	private final Object monitor = new Object();
	private final CountDownLatch latch;

	/**
	 * Instantiates a new lock free executor.
	 *
	 * @param workers
	 *            the workers
	 * @param numPerWorker
	 *            the num per worker
	 * @param runnable
	 *            the runnable
	 */
	public LockFreeExecutor(final int workers, final int numPerWorker, final Runnable runnable) {
		this.workers = workers;
		this.runnable = runnable;
		this.numPerWorker = numPerWorker;
		this.latch = new CountDownLatch(workers);
	}

	/**
	 * Run.
	 */
	public void run() {
		for (int i = 0; i < this.numPerWorker; i++) {
			this.runnable.run();
		}
	}

	/**
	 * Execute timed.
	 *
	 * @return the long
	 */
	public long executeTimed() {
		Thread[] threads = new Thread[this.workers];
		Stopwatch stopwatch = Stopwatch.createStarted();
		for (int i = 0; i < this.workers; i++) {
			threads[i] = new Thread(this::run);
			threads[i].start();
		}

		for (int i = 0; i < this.workers; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return stopwatch.elapsed(TimeUnit.MILLISECONDS);
	}
}
