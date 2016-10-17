package io.korhner.uuid_benchmark.generator.java;

import java.util.UUID;

import io.korhner.uuid_benchmark.generator.ThreadLocalGenerator;

public class JavaUuid extends ThreadLocalGenerator<Void> {

	@Override
	public String generate() {
		return UUID.randomUUID().toString();
	}

	@Override
	protected Void createGenerator() {
		return null;
	}

}
