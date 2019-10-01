package fr.umlv.java.inside;

import static java.util.stream.Collectors.joining;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Comparator;

public class Main {

	private static String propertyName(String name) {
		return Character.toLowerCase(name.charAt(3)) + name.substring(4);
	}

	private static String fieldAndFieldValue(Method e, Object obj){
			try {
				return propertyName(e.getName()) + ":" + e.invoke(obj);
			} catch (IllegalAccessException e1) {
				throw new IllegalStateException(e1);
			}
			catch (InvocationTargetException e2) {
				var cause = e2.getCause();
				if(cause instanceof RuntimeException)
					throw (RuntimeException)cause;
				if(cause instanceof Error)
					throw (Error)cause;
				throw new UndeclaredThrowableException(e2);
			}
	}

	public static String toJSON(Object obj) {
		var listOfMethods = obj.getClass().getMethods();
		return Arrays.stream(listOfMethods)
				.filter(e -> e.getName().startsWith("get"))
				.map(e -> fieldAndFieldValue(e, obj))
				.sorted(Comparator.naturalOrder())
				.collect(joining(",\n\t", "{\n\t", "\n}"));
	}

	/*
	 * public static String toJSON(Person person) { return "{\n" +
	 * "  \"firstName\": \"" + person.getFirstName() + "\"\n" + "  \"lastName\": \""
	 * + person.getLastName() + "\"\n" + "}\n"; }
	 * 
	 * public static String toJSON(Alien alien) { return "{\n" + "  \"planet\": \""
	 * + alien.getPlanet() + "\"\n" + "  \"members\": \"" + alien.getAge() + "\"\n"
	 * + "}\n"; }
	 */

	public static void main(String[] args) {
		var person = new Person("John", "Doe");
		System.out.println(toJSON(person));
		var alien = new Alien("E.T.", 100);
		System.out.println(toJSON(alien));
	}
}