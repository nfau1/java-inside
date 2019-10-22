package fr.umlv.java.inside;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class StringSwitchExampleTests {

//	@Test
//	@Tag("Q3")
//	public void TeststringSwitch() {
//		assertAll(
//				() -> assertEquals(0, StringSwitchExample.stringSwitch("foo")),
//				() -> assertEquals(1, StringSwitchExample.stringSwitch("bar")),
//				() -> assertEquals(2, StringSwitchExample.stringSwitch("bazz")),
//				() -> assertEquals(-1, StringSwitchExample.stringSwitch("other")));
//	}
//	
	
	@ParameterizedTest
	@Tag("Q5")
	@MethodSource("stringIntAndListProvider")
	void TeststringSwitch2(ToIntFunction<String> fun) {
		assertAll(
		() -> assertEquals(0, fun.applyAsInt("foo")),
		() -> assertEquals(1, fun.applyAsInt("bar")),
		() -> assertEquals(2, fun.applyAsInt("bazz")),
		() -> assertEquals(-1, fun.applyAsInt("other")));
	}

	static Stream<ToIntFunction<String>> stringIntAndListProvider() {
	    return Stream.of(
	 	   StringSwitchExample::stringSwitch,
	       StringSwitchExample::stringSwitch2
	       
	    );
	}

}
