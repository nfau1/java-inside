package fr.umlv.java.inside;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.math3.analysis.function.Log;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class LoggerTests {
	private static class A {
		private static final StringBuilder str = new StringBuilder();
		private static final Logger log = Logger.of(A.class, msg -> str.append(msg));
	}

	@Test
	@Tag("Q2")
	public void logString() {
		A.log.log("test1");
		assertEquals("test1", A.str.toString());
	}

	@Test
	@Tag("Q2")
	public void logNull() {
		assertAll(() -> assertThrows(NullPointerException.class, () -> Logger.of(null, __ -> {
		}).log("")), () -> assertThrows(NullPointerException.class, () -> Logger.of(LoggerTests.class, null).log("")));
	}

	@Test
	@Tag("Q2")
	public void logParameterNull() {
		assertThrows(NullPointerException.class, () -> Logger.of(LoggerTests.class, __ -> {
		}).log(null));
	}

	private static class B {
		private static final StringBuilder str = new StringBuilder();
		private static final Logger log = Logger.of(B.class, msg -> str.append(msg));
	}

	@Test
	@Tag("Q7")
	public void logDisable() {
		Logger.enable(B.class, false);
		B.log.log("test1");
		assertEquals("", B.str.toString());
	}

	@Test
	@Tag("Q7")
	public void logEnable() {
		Logger.enable(B.class, true);
		B.log.log("test1");
		assertEquals("test1", B.str.toString());
	}
//	
//	private static class C {
//		private static final StringBuilder str = new StringBuilder();
//		private static final Logger log = Logger.of(C.class, msg ->{str.delete(0, str.length()); str.append(msg);});
//		private static final Logger log_2 = Logger.of(C.class, msg ->str.delete(0, str.length()));
//
//	}
//
//	@Test
//	@Tag("Q9")
//		public void threads() {
//			Thread t1 = new Thread(() -> {
//				Logger.enableAndLog(C.class, false,(str) -> C.log_2.log(str), "test");
//				assertEquals("", C.str.toString());
//			});
//	
//			Thread t2 = new Thread(() -> {
//				Logger.enableAndLog(C.class, true,(str) -> C.log.log(str), "test");
//				assertEquals("test", C.str.toString());
//				});	
//			
//			Thread t3 = new Thread(() -> {
//				Logger.enableAndLog(C.class, true,(str) -> C.log.log(str), "test");
//				assertEquals("test", C.str.toString());
//				});
//
//			t1.start();
//			t2.start();
//		}
//	
	/******************** Question 10 *********************/
	
	private static class D {
		private static final StringBuilder str = new StringBuilder();
		private static final Logger log = Logger.of(D.class, msg -> str.append(msg));
	}
}
