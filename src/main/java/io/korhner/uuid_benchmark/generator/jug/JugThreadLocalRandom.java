package io.korhner.uuid_benchmark.generator.jug;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;

import io.korhner.uuid_benchmark.generator.ThreadLocalGenerator;

public class JugThreadLocalRandom extends ThreadLocalGenerator<RandomBasedGenerator> {

	@Override
	public String generate() {
		return getGenerator().generate().toString();
	}

	@Override
	protected RandomBasedGenerator createGenerator() {
		return Generators.randomBasedGenerator(ThreadLocalRandom.current());
	}

}
