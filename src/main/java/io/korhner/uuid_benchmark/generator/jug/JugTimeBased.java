package io.korhner.uuid_benchmark.generator.jug;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import io.korhner.uuid_benchmark.generator.ThreadLocalGenerator;

public class JugTimeBased extends ThreadLocalGenerator<TimeBasedGenerator> {

	@Override
	public String generate() {
		return getGenerator().generate().toString();
	}

	@Override
	protected TimeBasedGenerator createGenerator() {
		return Generators.timeBasedGenerator();
	}

}
