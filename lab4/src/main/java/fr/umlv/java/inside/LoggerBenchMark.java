package fr.umlv.java.inside;


import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;


@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class LoggerBenchMark {

	private static class A {
		private static final StringBuilder str = new StringBuilder();
		private static final Logger log = Logger.fastOf(A.class, __ -> {
		});
	}

	private static class B {
		private static final StringBuilder str = new StringBuilder();
		private static final Logger log = Logger.of(A.class, __ -> {
		});
	}

	private static class C {
		private static final StringBuilder str = new StringBuilder();
		private static final Logger log = Logger.fastOf(C.class, msg -> str.append(msg));
		static {
			Logger.enable(C.class, false);
		}
	}
	

	private static class D {
		private static final StringBuilder str = new StringBuilder();
		private static final Logger log = Logger.of(D.class, msg -> str.append(msg));
		static {
			Logger.enable(D.class, false);
		}
	}

	@Benchmark
	public void faster_logger_disabled() {
		C.log.log("");
	}
	

	@Benchmark
	public void simple_logger_disabled() {
		D.log.log("");
	}

	@Benchmark
	public void no_op() {
		// empty
	}

	@Benchmark
	public void simple_logger() {
		B.log.log("OK I WILL DO IT");
	}

	@Benchmark
	public void faster_logger() {
		A.log.log("OK I WILL DO IT");
	}
}