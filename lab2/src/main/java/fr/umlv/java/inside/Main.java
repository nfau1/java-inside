package fr.umlv.java.inside;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

	private static String propertyName(String name) {
		return Character.toLowerCase(name.charAt(3)) + name.substring(4);
	}

	public static String toJSON(Object obj) {
		var listOfMethods = obj.getClass().getMethods();
		return Arrays.stream(listOfMethods)
			.map(e -> e.getName())
			.filter(e -> e.substring(0, 3).equals("get"))
			.map(Main::propertyName)
			.collect(Collectors.joining(" "));
	}
	/*public static String toJSON(Person person) {
		return "{\n" + "  \"firstName\": \"" + person.getFirstName() + "\"\n" + "  \"lastName\": \""
				+ person.getLastName() + "\"\n" + "}\n";
	}

	public static String toJSON(Alien alien) {
		return "{\n" + "  \"planet\": \"" + alien.getPlanet() + "\"\n" + "  \"members\": \"" + alien.getAge() + "\"\n"
				+ "}\n";
	}*/

	public static void main(String[] args) {
		var person = new Person("John", "Doe");
		System.out.println(toJSON(person));
		//var alien = new Alien("E.T.", 100);
		//System.out.println(toJSON(alien));
	}
}