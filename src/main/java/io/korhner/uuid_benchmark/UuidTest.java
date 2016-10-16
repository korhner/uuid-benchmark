package io.korhner.uuid_benchmark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Sets;

/**
 * The Class UuidTest.
 */
public final class UuidTest {

	private static void waitForEnter(final String text) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print(text);
		br.readLine();
	}

	private static void measure(final Uuids uuid, final int numTimes, final int workers, final boolean interactive,
			final boolean trackCollisions) throws IOException, InterruptedException {
		if (interactive) {
			waitForEnter(String.format("Press enter to continue test %s", uuid.name()));
		}
		final Set<String> collisionSet = Sets.newConcurrentHashSet();
		final AtomicInteger collisionNum = new AtomicInteger();
		LockFreeExecutor executor = new LockFreeExecutor(workers, numTimes, new Runnable() {

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
		System.out.println(String.format("Generator %s took %d ms to generate %d uuids with %d workers. Collisions: %d",
				uuid.name(), millis, numTimes, workers, collisionNum.get()));
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
		waitForEnter("Press enter to start test");

		// a test without collision tracking, best performance
		System.out.println("Without collisions");
		for (Uuids uuid : Uuids.values()) {
			measure(uuid, 1000000, 10, false, false);
		}

		// a test with collision tracking, 'correctness'
		System.out.println("With collisions");
		for (Uuids uuid : Uuids.values()) {
			measure(uuid, 1000000, 10, false, true);
		}
	}
}
