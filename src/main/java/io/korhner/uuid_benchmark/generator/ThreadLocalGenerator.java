package io.korhner.uuid_benchmark.generator;

/**
 * The Interface UuidGenerator.
 */
public abstract class ThreadLocalGenerator<T> {

	private final ThreadLocal<T> generator;

	public ThreadLocalGenerator() {
		this.generator = new ThreadLocal<T>() {
			@Override
			protected T initialValue() {
				return createGenerator();
			}
		};
	}

	protected T getGenerator() {
		return this.generator.get();
	}
	
	public abstract String generate();

	protected abstract T createGenerator();
}
