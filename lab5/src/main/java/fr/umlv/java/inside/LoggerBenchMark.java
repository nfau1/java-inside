package fr.umlv.java.inside;


import java.util.concurrent.TimeUnit;
import java.util.function.ToIntFunction;

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
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class LoggerBenchMark {

	private void test(ToIntFunction<String> fnc){
		for(var i = 0; i < 100_000; i++) {
			fnc.applyAsInt("foo");
			fnc.applyAsInt("bar");
			fnc.applyAsInt("baz");
			fnc.applyAsInt("xD");
			fnc.applyAsInt("Nah nah nah");
			fnc.applyAsInt("yeeeeeeeeeeeee");
		}
	}


	@Benchmark
	public void StringSwitch1() {
		test(StringSwitchExample::stringSwitch);
	}
	@Benchmark
	public void StringSwitch2() {
		test(StringSwitchExample::stringSwitch2);
	}
	@Benchmark
	public void StringSwitch3() {
		test(StringSwitchExample::stringSwitch3);
	}
}