package io.korhner.uuid_benchmark;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.uuid.Generators;

/**
 * The Enum Uuids.
 */
public enum Uuids implements UuidGenerator {

	/**
	 * jdk implementation of type 4 uuid.
	 */
	JavaUuidGenerator() {

		@Override
		public String generate() {
			return UUID.randomUUID().toString();
		}

	},

	/**
	 * type 4 uuid with ThreadLocalRandom .
	 */
	JUG() {

		@Override
		public String generate() {

			return Generators.randomBasedGenerator(ThreadLocalRandom.current()).generate().toString();
		}
	}

}
