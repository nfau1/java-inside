package fr.umlv.java.inside;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

public class StringSwitchExample {

	private static final MethodHandle NOT_FOUND = MethodHandles.constant(int.class, -1);
	private static final MethodHandle EQUALS_MH;
	static {
		try {
			EQUALS_MH = MethodHandles.lookup().findVirtual(String.class, "equals",
					MethodType.methodType(boolean.class, Object.class));
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new AssertionError();
		}

	}

	public static int stringSwitch(String str) {
	switch (str) {
		case "foo" : return 0;
		case "bar" : return 1;
		case "bazz" : return 2;
		default : return -1;
		}
	}

	public static int stringSwitch2(String str) {
		try {
			var mh = createMHFromStrings("foo", "bar", "bazz");
			return (int) mh.invokeExact(str);
		} catch (RuntimeException | Error e) {
			throw e;
		} catch (Throwable e2) {
			throw new UndeclaredThrowableException(e2);
		}
	}

	private static MethodHandle createMHFromStrings(String... str) throws Throwable {

		var mh = MethodHandles.dropArguments(NOT_FOUND, 0, String.class);

		for (int i = 0; i < str.length; i++) {
			mh = MethodHandles.guardWithTest(MethodHandles.insertArguments(EQUALS_MH, 1, str[i]),
					MethodHandles.dropArguments(MethodHandles.constant(int.class, i), 0, String.class), mh);
		}
		return mh;
	}

	public static MethodHandle createMHFromStrings3(String... matches) {
		return new InliningCache(matches).dynamicInvoker();
	}
	
	public static int stringSwitch3(String str) {
		try {
			var mh = createMHFromStrings3("foo", "bar", "bazz");
			return (int) mh.invokeExact(str);
		} catch (RuntimeException | Error e) {
			throw e;
		} catch (Throwable e2) {
			throw new UndeclaredThrowableException(e2);
		}
	}


	static class InliningCache extends MutableCallSite {
		private static final MethodHandle SLOW_PATH;
		private final List<String> matches;

		static {
		      try {
				SLOW_PATH = MethodHandles.
						  	  lookup().
						  	  findVirtual(
						  			  InliningCache.class,
						  			  "slowPath", 
						  			  MethodType.methodType(int.class, String.class)
						  	  );
			} catch (NoSuchMethodException | IllegalAccessException e) {
				throw new AssertionError(e);
		    }
		}

		public InliningCache(String... matches) {
			super(MethodType.methodType(int.class, String.class));
			this.matches = List.of(matches);
			setTarget(MethodHandles.insertArguments(SLOW_PATH, 0, this));
		}
		
		@SuppressWarnings("unused")
		private int slowPath(String value) {
			var index = this.matches.indexOf(value);
			
			var mh = MethodHandles.guardWithTest(
					MethodHandles.insertArguments(EQUALS_MH, 1, value),
					MethodHandles.dropArguments(MethodHandles.constant(int.class, index), 0, String.class),
					this.getTarget());
			setTarget(mh);
			return index;
		}
		
//		private int slowPath_2(String value) {
//			var index = this.matches.indexOf(value);
//			var mh = this.getTarget();
//			mh.
//			return index;
//			
//		}
	}

}
