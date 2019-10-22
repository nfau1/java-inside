package fr.umlv.java.inside;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

public class StringSwitchExample {

	private static final MethodHandle NOT_FOUND = MethodHandles.constant(int.class, -1);
	private static final MethodHandle EQUALS_MH;
	static {
		try {
			EQUALS_MH = MethodHandles.
					 lookup().
					 findVirtual(String.class, "equals", MethodType.methodType(boolean.class, Object.class));
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new AssertionError();
		}
	
	}
	public static int stringSwitch(String str) {
		return switch (str) {
		case "foo" -> 0;
		case "bar" -> 1;
		case "bazz" -> 2;
		default -> -1;
		};
	}

	public static int stringSwitch2(String str){
		try {
			var mh = createMHFromStrings("foo", "bar", "bazz");
			return (int) mh.invokeExact(str);
		} catch(RuntimeException | Error e) {
				throw e;
		}catch (Throwable e2){
			throw new UndeclaredThrowableException(e2);
		}
	}

	private static MethodHandle createMHFromStrings(String... str) throws Throwable {
	
		var mh = MethodHandles.dropArguments(NOT_FOUND, 0, String.class);
		
		for(int i = 0; i < str.length;i++) {
			mh = MethodHandles.guardWithTest(
					MethodHandles.insertArguments(EQUALS_MH, 1, str[i]),
					MethodHandles.dropArguments(MethodHandles.constant(int.class, i), 0, String.class),
					mh);
		}
		return mh;
	}
}
