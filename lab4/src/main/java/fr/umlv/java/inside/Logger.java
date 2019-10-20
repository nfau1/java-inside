package fr.umlv.java.inside;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Objects;
import java.util.function.Consumer;

public interface Logger {
	public void log(String message);
//	final Object lock = new Object();

	public static final ClassValue<MutableCallSite> ENABLE_CALLSITES = new ClassValue<>() {
        protected MutableCallSite computeValue(Class<?> type) {
        	var callSite = new MutableCallSite(MethodHandles.constant(boolean.class, true));
        	MutableCallSite array [] =  new MutableCallSite[]{callSite};
   	      	MutableCallSite.syncAll(array);
   	      	callSite.setTarget(MethodHandles.constant(boolean.class, true));
   	        return callSite;
        }
    };

    public static void enable(Class<?> declaringClass, boolean enable) {
    	MutableCallSite array [] =  new MutableCallSite[]{ENABLE_CALLSITES.get(declaringClass)};
	    MutableCallSite.syncAll(array);
	    array[0].setTarget(MethodHandles.constant(boolean.class, enable));
    }
    
	public static Logger of(Class<?> declaringClass, Consumer<? super String> consumer) {
		Objects.requireNonNull(declaringClass);
		Objects.requireNonNull(consumer);	

		var mh = createLoggingMethodHandle(declaringClass, consumer);
		return new Logger() {
			@Override
			public void log(String message) {
				Objects.requireNonNull(message);
				try {
					mh.invokeExact(message);
				} catch (Throwable t) {
					System.out.println(t);
					if (t instanceof RuntimeException) {
						throw (RuntimeException) t;
					}
					if (t instanceof Error) {
						throw (Error) t;
					}
					throw new UndeclaredThrowableException(t);
				}
			}
		};
	}

	public static Logger fastOf(Class<?> declaringClass, Consumer<? super String> consumer) {
		Objects.requireNonNull(declaringClass);
		Objects.requireNonNull(consumer);
		var mh = createLoggingMethodHandle(declaringClass, consumer);

		return (message) -> {
			Objects.requireNonNull(message);
			try {
				mh.invokeExact(message);
			} catch (Throwable t) {
				System.out.println(t);
				if (t instanceof RuntimeException) {
					throw (RuntimeException) t;
				}
				if (t instanceof Error) {
					throw (Error) t;
				}
				throw new UndeclaredThrowableException(t);
			}
		};
	}

	private static MethodHandle createLoggingMethodHandle(Class<?> declaringClass, Consumer<? super String> consumer) {
		var lookup = MethodHandles.lookup();
		MethodHandle mh;

		try {
			mh = lookup.findVirtual(Consumer.class, "accept", MethodType.methodType(void.class, Object.class));
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new AssertionError(e);
		}
		mh = MethodHandles.insertArguments(mh, 0, consumer);
		mh = mh.asType(MethodType.methodType(void.class, String.class));
		
		return MethodHandles.guardWithTest(
				ENABLE_CALLSITES.get(declaringClass).dynamicInvoker(),
				mh,
				MethodHandles.empty(MethodType.methodType(void.class, String.class))
		);		
	}

//	public static void enableAndLog(Class<?> c, boolean b, Consumer<String> func, String str) {
//		synchronized (lock) {
//			Logger.enable(c, b);
//			func.accept(str);
//		}
//	}
}
