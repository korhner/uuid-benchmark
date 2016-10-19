package io.korhner.uuid_benchmark;

public final class TestResult implements Comparable<TestResult> {

	private final String generatorName;
	private final long totalDuration;
	final int uuidsPerWorker;
	private final int workers;
	private final int collisions;

	public TestResult(final String generatorName, final long totalDuration, final int uuidsPerWorker, final int workers,
			final int collisions) {
		this.generatorName = generatorName;
		this.totalDuration = totalDuration;
		this.uuidsPerWorker = uuidsPerWorker;
		this.workers = workers;
		this.collisions = collisions;
	}

	@Override
	public String toString() {
		double speed = (this.workers * this.uuidsPerWorker) / (this.totalDuration / 1000f);
		return String.format(
				"Generator %s took %d ms to generate %d uuids with %d workers. Speed: %.2f/sec Collisions: %d",
				this.generatorName, this.totalDuration, this.uuidsPerWorker * this.workers, this.workers, speed,
				this.collisions);
	}

	@Override
	public int compareTo(TestResult o) {
		return Long.compare(this.totalDuration, o.totalDuration);
	}
}
