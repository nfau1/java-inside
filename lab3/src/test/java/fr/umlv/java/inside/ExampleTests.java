package fr.umlv.java.inside;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import fr.umlv.java.inside.Example;
import java.lang.reflect.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.*;

public class ExampleTests {

	@Test @Tag("Q3")
	public void TestAStaticHelloReflect() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
		var val = 12;
		var meth =  Example.class.getDeclaredMethod("aStaticHello", int.class);
		meth.setAccessible(true);
		assertEquals("question " + val, meth.invoke(null,val)) ;
	}
	
	@Test @Tag("Q3")
	public void TestAnInstanceHelloReflect() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
		var val = 12;
		var meth =  Example.class.getDeclaredMethod("anInstanceHello", int.class);
		meth.setAccessible(true);
		assertEquals("question " + val, meth.invoke(new Example(),val)) ;
	}
	
	@Test @Tag("Q4")
	public void TestAStaticHelloInvokeFindStatic() throws Throwable{
		var val = 12;
		var lookup = MethodHandles.privateLookupIn(Example.class, MethodHandles.lookup());
		var meth = lookup.findStatic(Example.class, "aStaticHello", MethodType.methodType(String.class, int.class));
		assertEquals("question " + val, (String)meth.invokeExact(val));
	}
	
	
	@Test @Tag("Q5")
	public void TestAnInstanceHelloInvokeFindVirtual() throws Throwable{
		var val = 12;
		var lookup = MethodHandles.privateLookupIn(Example.class, MethodHandles.lookup());
		var meth = lookup.findVirtual(Example.class, "anInstanceHello", MethodType.methodType(String.class, int.class));
		assertEquals("question " + val, (String)meth.invokeExact(new Example(), val));
	}
	
	@Test @Tag("Q6")
	public void TestAnInstanceHelloInvokeInsertArguments() throws Throwable{
		var val = 1;
		var lookup = MethodHandles.privateLookupIn(Example.class, MethodHandles.lookup());
		var meth = lookup.findVirtual(Example.class, "anInstanceHello", MethodType.methodType(String.class, int.class));
		meth = MethodHandles.insertArguments(meth, 1, val);
		assertEquals("question " + val, (String)meth.invokeExact(new Example()));
	}
	
	@Test @Tag("Q7")
	public void TestAnInstanceHelloInvokeDropArguments() throws Throwable{
		var val = 1;
		var lookup = MethodHandles.privateLookupIn(Example.class, MethodHandles.lookup());
		var meth = lookup.findVirtual(Example.class, "anInstanceHello", MethodType.methodType(String.class, int.class));
		meth = MethodHandles.insertArguments(meth, 1, val);
		meth = MethodHandles.dropArguments(meth,1, int.class);
		assertEquals("question " + val, (String)meth.invokeExact(new Example(), 1));
	}
	
	@Test @Tag("Q8")
	public void TestAnInstanceHelloInvokeAsType() throws Throwable{
		var val = 1;
		var lookup = MethodHandles.privateLookupIn(Example.class, MethodHandles.lookup());
		var meth = lookup.findVirtual(Example.class, "anInstanceHello", MethodType.methodType(String.class, int.class));
		meth = meth.asType(MethodType.methodType(String.class, Example.class, Integer.class));
		assertEquals("question " + val, (String)meth.invokeExact(new Example(), Integer.valueOf(val)));
	}
	
	@Test @Tag("Q9")
	public void TestAnInstanceHelloInvokeConstant() throws Throwable{
		var meth = MethodHandles.constant(String.class, "question 9");
		assertEquals("question 9", (String)meth.invokeExact());
	}
	
	@Test @Tag("Q10")
	public void TestAnInstanceHelloInvokeTestFail() throws Throwable{
		var val = (Object)"pasfoo";
		var lookup = MethodHandles.publicLookup();
		var meth = lookup.findVirtual(String.class,"equals",  MethodType.methodType(boolean.class, Object.class));



		var methode = MethodHandles.guardWithTest(
				meth,
				MethodHandles.dropArguments(MethodHandles.constant(int.class, 1), 0, String.class, Object.class),  
				MethodHandles.dropArguments(MethodHandles.constant(int.class, -1), 0, String.class, Object.class)
		);
		
		methode = MethodHandles.insertArguments(methode, 1, val);
		
		assertEquals(-1, (int)methode.invokeExact(new String("foo")));
	}
	
	@Test @Tag("Q10")
	public void TestAnInstanceHelloInvokeTestSuccess() throws Throwable{
		var val = "foo";
		var lookup = MethodHandles.publicLookup();
		var meth = lookup.findVirtual(String.class,"equals",  MethodType.methodType(boolean.class, Object.class));

		var methode = MethodHandles.guardWithTest(
				meth.asType(MethodType.methodType(boolean.class, String.class, String.class)),
				MethodHandles.dropArguments(MethodHandles.constant(int.class, 1), 0, String.class, String.class),  
				MethodHandles.dropArguments(MethodHandles.constant(int.class, -1), 0, String.class, String.class)
		);
		
		methode = MethodHandles.insertArguments(methode, 1, val);
		assertEquals(1, (int)methode.invokeExact("foo"));
	}
	
}
