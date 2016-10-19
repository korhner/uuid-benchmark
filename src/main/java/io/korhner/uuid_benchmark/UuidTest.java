package io.korhner.uuid_benchmark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.uuid.Logger;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import io.korhner.uuid_benchmark.generator.ThreadLocalGenerator;
import io.korhner.uuid_benchmark.generator.java.JavaUuid;
import io.korhner.uuid_benchmark.generator.jug.JugRandom;
import io.korhner.uuid_benchmark.generator.jug.JugThreadLocalRandom;
import io.korhner.uuid_benchmark.generator.jug.JugTimeBased;

/**
 * The Class UuidTest.
 */
public final class UuidTest {

	private static void waitForEnter(final String text) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print(text);
		br.readLine();
	}

	private static TestResult measure(final ThreadLocalGenerator<?> uuid, final int uuidsPerWorker, final int workers,
			final boolean trackCollisions) throws IOException, InterruptedException {

		final Set<String> collisionSet = Sets.newConcurrentHashSet();
		final AtomicInteger collisionNum = new AtomicInteger();
		LockFreeExecutor executor = new LockFreeExecutor(workers, uuidsPerWorker, new Runnable() {

			@Override
			public void run() {
				String uuidString = uuid.generate();
				if (trackCollisions) {
					if (!collisionSet.add(uuidString)) {
						collisionNum.incrementAndGet();
					}
				}
			}
		});
		long millis = executor.executeTimed();
		return new TestResult(uuid.getClass().getSimpleName(), millis, uuidsPerWorker, workers, collisionNum.get());
	}

	private static List<ThreadLocalGenerator<?>> createGenerators() {
		return ImmutableList.of(new JavaUuid(), new JavaUuid(), new JugRandom(), new JugThreadLocalRandom(),
				new JugTimeBased());
	}

	public static void performTest(List<ThreadLocalGenerator<?>> generators, int uuidsPerWorker, int workers,
			boolean trackCollisions) throws IOException, InterruptedException {
		System.out.println(String.format("Running test with %d uuids per worker on %d workers. Track collisions: %s",
				uuidsPerWorker, workers, trackCollisions));

		List<TestResult> results = new ArrayList<TestResult>();
		for (ThreadLocalGenerator<?> uuid : generators) {
			results.add(measure(uuid, uuidsPerWorker, workers, trackCollisions));
		}
		Collections.sort(results);
		results.stream().forEach(x -> System.out.println(x));
		System.out.println("Test ended.\n");
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public static void main(final String[] args) throws IOException, InterruptedException {
		Logger.setLogger(null);

		waitForEnter("Press enter to start test");

		// a test with collision tracking, 'correctness'
		performTest(createGenerators(), 100000, 15, true);

		// a test without collision tracking, concurrent
		performTest(createGenerators(), 100000, 15, false);

		// a test without collision tracking, single thread
		performTest(createGenerators(), 1000000, 1, false);

	}

}
