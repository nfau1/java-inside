package fr.umlv.java.inside;

import static java.util.stream.Collectors.joining;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.lang.ClassValue;

public class Main {	

	private final static ClassValue<Function<Object, String>> cacheMethods = new ClassValue<Function<Object, String>>() {
		@Override
		protected Function<Object, String> computeValue(Class<?> type) {
			var methods = Arrays.stream(type.getMethods())
								.filter(e -> e.getName().startsWith("get") && e.isAnnotationPresent(JSONProperty.class))
								.sorted(Comparator.comparing(Method::getName))
								.collect(Collectors.toList());
			
			var map = methods.stream()
								.collect(Collectors.toUnmodifiableMap(e -> e.getName(),
										e -> getAnnotationNameOrMethodName(e)));
								
			return obj -> methods.stream()
								 .map(e -> fieldAndFieldValue(e, obj,map.get(e.getName())))
								 .collect(joining(",\n\t", "{\n\t", "\n}"));
		}
	};
		
	public static String toJSON(Object obj) {
		return cacheMethods.get(obj.getClass()).apply(obj);
	}

	
	private static String propertyName(String name) {
		return Character.toLowerCase(name.charAt(3)) + name.substring(4);
	}
	
	private static String getAnnotationNameOrMethodName(Method e){
		
		var jsonProperty = e.getAnnotation(JSONProperty.class).value();
		return jsonProperty.isEmpty() ? 
				 propertyName(e.getName()) : jsonProperty;
	}

	private static String fieldAndFieldValue(Method e, Object obj, String name){		
			try {
				return name + ":" + e.invoke(obj);
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