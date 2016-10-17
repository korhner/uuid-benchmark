package io.korhner.uuid_benchmark.generator.jug;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;

import io.korhner.uuid_benchmark.generator.ThreadLocalGenerator;

public class JugRandom extends ThreadLocalGenerator<RandomBasedGenerator> {

	@Override
	public String generate() {
		return getGenerator().generate().toString();
	}

	@Override
	protected RandomBasedGenerator createGenerator() {
		return Generators.randomBasedGenerator();
	}

}
